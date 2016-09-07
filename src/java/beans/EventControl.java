package beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Dragan
 */
@ManagedBean
@SessionScoped
public class EventControl implements Serializable {

    @ManagedProperty(value = "#{login}")
    private Login login;

    private Date calendarValue = null;
    private Date calendarReservationValue = null;
    private Date displayCalendarValue = null;
    private Date displayCalendarReservationValue = null;

    private BelgradeEvent newEvent = new BelgradeEvent();
    private BelgradeEventLocation eventLocation;
    private int locationId = -1;

    private List<BelgradeEvent> allEvents = new LinkedList<>();

    private HashMap<String, BelgradeEvent> allEventsTitles = new HashMap<String, BelgradeEvent>();

    private String displayEventTitle = "";
    private BelgradeEvent displayEvent = null;

    private Category newCategory = new Category();
    
    private List<StatisticElement> eventsStatistics = new LinkedList<>();
    
    private List<Category> newEventsCategories = new LinkedList<>();

    public void updateCashierLocation() {
        Connection con = null;
        Statement st = null;
        Statement st2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select locationid from cashiers where userid=" + login.getUser().getId();

            rs = st.executeQuery(query);

            if (rs.next()) {

                locationId = rs.getInt("locationid");

                query = "select * from locations where id=" + locationId;

                st2 = con.createStatement();

                rs2 = st2.executeQuery(query);

                if (rs2.next()) {

                    eventLocation = new BelgradeEventLocation();

                    eventLocation.setTitle(rs2.getString("title"));
                    eventLocation.setAddress(rs2.getString("address"));
                    eventLocation.setCapacity(rs2.getInt("capacity"));

                }

                rs2.close();
                st2.close();
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gatherEventsData() {

        allEvents.clear();

        allEventsTitles.clear();
        
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select * from events where locationid=" + locationId;

            rs = st.executeQuery(query);

            while (rs.next()) {

                BelgradeEvent event = new BelgradeEvent();

                event.setId(rs.getInt("id"));
                event.setTitle(rs.getString("title"));
                event.setDate(rs.getTimestamp("date"));
                event.setLocation(eventLocation);
                event.setCanceled(rs.getBoolean("canceled"));

                allEvents.add(event);

                allEventsTitles.put(event.getTitle(), event);

            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateDisplayEvent() {

        for (BelgradeEvent e : allEvents) {
            if (e.getTitle().equals(displayEventTitle)) {
                displayEvent = e;
            }
        }
    }

    public String addEvent() {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "insert into events(title, locationid, date, canceled, soldTickets, resenable, maxres, description) values('"
                    + newEvent.getTitle() + "', " + locationId + ", '" + newEvent.getDate() + "', " + false + "," + 0
                    + ",'" + newEvent.getReservationEnableDate() + "'," + newEvent.getMaxReservations() + ", '"
                    + newEvent.getDescription() + "')";

            st.executeUpdate(query);
            
            query = "select id from events where title='" + newEvent.getTitle() + "'";
            
            rs = st.executeQuery(query);
            
            int id = 0;
            
            if (rs.next()) {
                id = rs.getInt("id");
            }
            
            for (Category c : newEventsCategories) {
                
                query = "insert into categories(eventid, name, size, price) values(" + id + ",'" + c.getName() + "'," 
                        + c.getSize() + "," + c.getTicketPrice() + ")";
                
                st.executeUpdate(query);
                
            }
            
            for (BelgradeEventFile f : newEvent.getFiles()) {
                
                query = "insert into files(eventid, filename, filepath) values(" + id + ",'" + f.getName() + "','"
                        + f.getPath() + "')";
                
                st.executeUpdate(query);
                
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index?faces-redirect=true";
    }

    public void changeEventDetails() {

        Connection con = null;
        Statement st = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "update events set title='" + displayEvent.getTitle() + "', date='" + displayEvent.getDate()
                    + "', resenable='" + displayEvent.getReservationEnableDate() + "', maxres=" + displayEvent.getMaxReservations()
                    + " where id=" + allEventsTitles.get(displayEventTitle).getId();

            st.executeUpdate(query);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Izmena je uspesna!"));

            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void postponeEvent() {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "update events set canceled=1 where id=" + allEventsTitles.get(displayEventTitle).getId();

            st.executeUpdate(query);

            query = "select * from reservations where eventid=" + allEventsTitles.get(displayEventTitle).getId();

            rs = st.executeQuery(query);

            while (rs.next()) {

                String cashRelatedText = rs.getBoolean("realized") ? "Mozete podici Vas novac na blagajni. " : "";

                String text = "Dogadjaj " + displayEventTitle + " je otkazan. " + cashRelatedText + "Vas BelgradeEvent Tim.";

                query = "insert into messagess(user, text) values('" + rs.getString("user") + "','" + text + "')";

            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Dogadjaj je otkazan - Registrovani korisnici su obavesteni!"));

            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addCategory() {
       
        Category category = new Category();
        
        newEventsCategories.add(category);
        
    }

    public void deleteEvent() {

        Connection con = null;
        Statement st = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "delete from events where id=" + allEventsTitles.get(displayEventTitle).getId();

            st.executeUpdate(query);

            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void handleFileUpload(FileUploadEvent event) throws IOException {

        FacesMessage msg = new FacesMessage("Upload", event.getFile().getFileName() + " je postavljen na server.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        Random r=new Random();
        int broj=r.nextInt(100000);
        File result = new File(extContext.getRealPath("//resources//uploads//" + Integer.toString(broj)+event.getFile().getFileName()));
        UploadedFile file = event.getFile();
        try {

            FileOutputStream fos = new FileOutputStream(result);
            InputStream is = file.getInputstream();
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];
            int a;
            while (true) {
                a = is.read(buffer);
                if (a < 0) {
                    break;
                }
                fos.write(buffer, 0, a);
                fos.flush();
            }
            fos.close();
            is.close();

            if (newEvent.getFiles() == null) {
                newEvent.setFiles(new LinkedList<BelgradeEventFile>());
            }

            BelgradeEventFile af = new BelgradeEventFile();
            af.setName(event.getFile().getFileName());
            af.setPath(Integer.toString(broj)+event.getFile().getFileName());

            newEvent.getFiles().add(af);
        } catch (IOException e) {
        }
    }

    public void deleteFile(BelgradeEventFile af) {

        newEvent.getFiles().remove(af);

    }
    
    public void gatherEventsStatistics() {

        eventsStatistics.clear();
        
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select eventname, avg(grade) as grd from commentsgrades group by eventname";

            rs = st.executeQuery(query);

            while (rs.next()) {

                StatisticElement statisticElement = new StatisticElement();
                
                statisticElement.setParam(rs.getString("eventname"));
                statisticElement.setStat(rs.getInt("grd"));
                
                eventsStatistics.add(statisticElement);
                
            }

            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public BelgradeEvent getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(BelgradeEvent newEvent) {
        this.newEvent = newEvent;
    }

    public BelgradeEventLocation getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(BelgradeEventLocation eventLocation) {
        this.eventLocation = eventLocation;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public List<BelgradeEvent> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(List<BelgradeEvent> allEvents) {
        this.allEvents = allEvents;
    }

    public String getDisplayEventTitle() {
        return displayEventTitle;
    }

    public void setDisplayEventTitle(String displayEventId) {
        this.displayEventTitle = displayEventId;
    }

    public BelgradeEvent getDisplayEvent() {
        return displayEvent;
    }

    public void setDisplayEvent(BelgradeEvent displayEvent) {
        this.displayEvent = displayEvent;
    }

    public HashMap<String, BelgradeEvent> getAllEventsTitles() {
        return allEventsTitles;
    }

    public void setAllEventsTitles(HashMap<String, BelgradeEvent> allEventsTitles) {
        this.allEventsTitles = allEventsTitles;
    }

    public Date getCalendarValue() {
        return calendarValue;
    }

    public void setCalendarValue(Date calendarValue) {
        this.calendarValue = calendarValue;
        this.calendarReservationValue = new Date(calendarValue.getTime() - 3 * 86400000);
        this.newEvent.setDate(new Timestamp(calendarValue.getTime()));
    }

    public Date getDisplayCalendarValue() {
        displayCalendarValue = new Date(displayEvent.getDate().getTime());
        return displayCalendarValue;
    }

    public void setDisplayCalendarValue(Date displayCalendarValue) {
        this.displayCalendarValue = displayCalendarValue;
        this.displayEvent.setDate(new Timestamp(displayCalendarValue.getTime()));
        this.displayCalendarReservationValue = new Date(displayCalendarValue.getTime() - 3 * 86400000);
    }

    public Date getCalendarReservationValue() {
        return calendarReservationValue;
    }

    public void setCalendarReservationValue(Date calendarReservationValue) {
        this.calendarReservationValue = calendarReservationValue;
        this.newEvent.setReservationEnableDate(new Timestamp(calendarReservationValue.getTime()));
    }

    public Date getDisplayCalendarReservationValue() {
        return displayCalendarReservationValue;
    }

    public void setDisplayCalendarReservationValue(Date displayCalendarReservationValue) {
        this.displayCalendarReservationValue = displayCalendarReservationValue;
        this.displayEvent.setReservationEnableDate(new Timestamp(displayCalendarReservationValue.getTime()));
    }

    public Category getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(Category newCategory) {
        this.newCategory = newCategory;
    }

    public List<StatisticElement> getEventsStatistics() {
        return eventsStatistics;
    }

    public void setEventsStatistics(List<StatisticElement> eventsStatistics) {
        this.eventsStatistics = eventsStatistics;
    }

    public List<Category> getNewEventsCategories() {
        return newEventsCategories;
    }

    public void setNewEventsCategories(List<Category> newEventsCategories) {
        this.newEventsCategories = newEventsCategories;
    }

    
    
    
}
