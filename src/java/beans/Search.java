/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Drazen
 */
@ManagedBean
@SessionScoped
public class Search implements Serializable {

    @ManagedProperty(value = "#{login}")
    private Login login;

    private String keyword = "";
    private Date startDate = new Date();
    private Date endDate = new Date(startDate.getTime() + 86400000);
    private String eventPlace = "";

    private List<BelgradeEvent> searchedEventsList = new LinkedList<>();
    private List<BelgradeEventLocation> eventsLocationList = new LinkedList<>();
    private List<String> selectedEventsLocationList = new LinkedList<>();
    private boolean submited = false;

    private String displayEventTitle = "";
    private BelgradeEvent displayEvent = null;

    private int ticketsNumber = 0;
    private List<Reservation> reservations = new LinkedList<>();

    private List<Reservation> boughtTickets = new LinkedList<>();

    private Timestamp today = new Timestamp(new Date().getTime());

    private List<Message> messages = new LinkedList<>();

    private List<Category> eventCategories = new LinkedList<>();
    private List<CategortyAndCards> categoryTickets = new LinkedList<>();

    private List<Reservation> commentsEnabledEvents = new LinkedList<>();

    private int grade = 0;

    private String comment = "";

    private List<CommentGrade> userComments = new LinkedList<>();

    private List<CommentGrade> eventCommentsGrades = new LinkedList<>();

    private List<StatisticElement> userStatistics = new LinkedList<>();
    private List<StatisticElement> userStatisticsDate = new LinkedList<>();

    public Search() {
        gatherLocationData();
    }

    public void gatherLocationData() {

        eventsLocationList.clear();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select * from locations";
            rs = st.executeQuery(query);

            while (rs.next()) {

                BelgradeEventLocation eventLocation = new BelgradeEventLocation();

                eventLocation.setTitle(rs.getString("title"));
                eventLocation.setAddress(rs.getString("address"));
                eventLocation.setCapacity(rs.getInt("capacity"));

                eventsLocationList.add(eventLocation);

            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doSearch() {

        submited = true;

        searchedEventsList.clear();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select * from events inner join locations as l on locationid = l.id";
            rs = st.executeQuery(query);

            while (rs.next()) {

                BelgradeEvent event = new BelgradeEvent();
                BelgradeEventLocation eventLocation = new BelgradeEventLocation();

                eventLocation.setId(rs.getInt("l.id"));
                eventLocation.setTitle(rs.getString("l.title"));
                eventLocation.setAddress(rs.getString("l.address"));
                eventLocation.setCapacity(rs.getInt("l.capacity"));

                event.setId(rs.getInt("id"));
                event.setTitle(rs.getString("title"));
                event.setLocation(eventLocation);
                event.setDate(rs.getTimestamp("date"));
                event.setSoldTickets(rs.getInt("soldtickets"));
                event.setCanceled(rs.getBoolean("canceled"));

                if (event.getTitle().contains(keyword.toLowerCase()) && event.getDate().after(startDate)
                        && event.getDate().before(endDate) && selectedEventsLocationList.contains(event.getLocation().toString())) {
                    searchedEventsList.add(event);
                }

            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDisplayEvent() {

        for (BelgradeEvent e : searchedEventsList) {
            if (e.getTitle().equals(displayEventTitle)) {
                displayEvent = e;
            }
        }

        eventCategories.clear();
        categoryTickets.clear();
        eventCommentsGrades.clear();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select * from categories where eventid=" + displayEvent.getId() + "";

            rs = st.executeQuery(query);

            while (rs.next()) {

                Category category = new Category();

                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setSize(rs.getInt("size"));
                category.setTicketPrice(rs.getInt("price"));
                category.setSoldTickets(rs.getInt("sold"));

                eventCategories.add(category);
                categoryTickets.add(new CategortyAndCards(category, 0));

            }

            query = "select * from files where eventid=" + displayEvent.getId();

            rs = st.executeQuery(query);

            displayEvent.getFiles().clear();

            while (rs.next()) {

                BelgradeEventFile f = new BelgradeEventFile();

                f.setId(rs.getInt("id"));
                f.setName(rs.getString("filename"));
                f.setPath(rs.getString("filepath"));

                displayEvent.getFiles().add(f);

            }

            query = "select * from commentsgrades where eventname='" + displayEventTitle + "'";

            rs = st.executeQuery(query);

            while (rs.next()) {

                CommentGrade commentGrade = new CommentGrade();

                commentGrade.setId(rs.getInt("id"));
                commentGrade.setUserId(rs.getInt("userid"));
                commentGrade.setEventname(rs.getString("eventname"));
                commentGrade.setComment(rs.getString("comment"));
                commentGrade.setGrade(grade);

                eventCommentsGrades.add(commentGrade);

            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void gatherReservationData() {

        reservations.clear();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select * from reservations as r, events as e, locations as l where r.eventid=e.id"
                    + " and r.locationid=l.id and user='" + login.getUser().getUsername() + "' ";

            rs = st.executeQuery(query);

            while (rs.next()) {

                Reservation reservation = new Reservation();

                reservation.setId(rs.getInt("r.id"));
                reservation.setUser(rs.getString("r.user"));
                reservation.setTickets(rs.getInt("r.tickets"));
                reservation.setEventid(rs.getInt("r.eventid"));
                reservation.setEventTitle(rs.getString("e.title"));
                reservation.setEventDate(rs.getTimestamp("e.date"));
                reservation.setLocationid(rs.getInt("locationid"));
                reservation.setLocationTitle(rs.getString("l.title"));
                reservation.setExpirationDate(rs.getTimestamp("r.expirationDate"));
                reservation.setRealized(rs.getBoolean("r.realized"));

                if (reservation.isRealized()) {
                    boughtTickets.add(reservation);
                } else {
                    reservations.add(reservation);
                }

            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeReservation() {

        for (CategortyAndCards c : categoryTickets) {
            ticketsNumber += c.getTickets();
        }

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select sum(amount) as sold from sellinfo where eventid=" + displayEvent.getId();

            rs = st.executeQuery(query);

            if (rs.next()) {

                int alreadySold = rs.getInt("sold");

                if (alreadySold + ticketsNumber > displayEvent.getLocation().getCapacity()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage("Ne postoji taj broj slobodnih ulaznica! - Preostalo "
                                    + (displayEvent.getLocation().getCapacity() - alreadySold) + " ulaznica"));

                    return;
                }
            }

            query = "select sum(tickets) from reservations where user='" + login.getUser().getUsername() + "' and "
                    + "eventid=" + displayEvent.getId() + " and locationid=" + displayEvent.getLocation().getId();

            rs = st.executeQuery(query);

            if (rs.next()) {

                int currentlyReserve = rs.getInt(1);

                if (currentlyReserve + ticketsNumber > displayEvent.getMaxReservations()) {

                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage("Vec ste rezervisali " + currentlyReserve + " karata."
                                            + " Dozvoljen broj rezervacija po korisniku za trazeni dogadjaj je: "
                                            + displayEvent.getMaxReservations() + "."));

                    return;
                }

            }

            query = "select * from reservations where realized=0 and user='" + login.getUser().getUsername() + "' and "
                    + "eventid=" + displayEvent.getId() + " and locationid=" + displayEvent.getLocation().getId()
                    + " order by expirationdate";

            rs = st.executeQuery(query);

            if (rs.first()) {

                if (rs.getTimestamp("expirationdate").before(new Timestamp(new Date().getTime()))) {

                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage("Imate nerealizovanih rezervacija za izabrani dogadjaj."
                                            + " Ne mozete da napravite nove!"));

                    return;
                }

            }

            String forMessage = "";

            for (CategortyAndCards c : categoryTickets) {
                forMessage += c.getCategory().getName() + " - " + c.getTickets()
                        + " karte po ceni od " + c.getCategory().getTicketPrice() + " din.";
            }

            // testing purpose: expiration date = today
            // for final version set this date to - today + 2
            query = "insert into reservations(user, tickets, eventid, locationid, expirationdate, realized) "
                    + " values('" + login.getUser().getUsername() + "'," + ticketsNumber + "," + displayEvent.getId()
                    + "," + displayEvent.getLocation().getId() + ",'" + new Timestamp((new Date()).getTime() + 120000)
                    + "'," + 0 + ")";

            st.executeUpdate(query);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Uspesno ste rezervisali " + ticketsNumber + " karata!"));

            String text = "Rezervisaliste " + ticketsNumber + " ulaznica za dogadjaj: " + displayEventTitle + " koji se odrzava "
                    + displayEvent.getDate() + " u " + displayEvent.getLocation().getTitle() + ". I to: " + forMessage
                    + " \nRezervacija Vam vazi do "
                    + new Date(today.getTime() + 2 * 86400000) + ". \nHvala na poverenju! \nBelgradeEvent Tim.";

            query = "insert into messages(user, text) values('" + login.getUser().getUsername() + "', '" + text + "')";

            st.executeUpdate(query);

            query = "select id from reservations where user='" + login.getUser().getUsername() + "' and eventid="
                    + displayEvent.getId();

            rs = st.executeQuery(query);

            if (rs.next()) {

                int resid = rs.getInt("id");

                for (CategortyAndCards c : categoryTickets) {

                    query = "insert into sellinfo(resid, eventid, catid, amount) values(" + resid + "," + displayEvent.getId()
                            + "," + c.getCategory().getId() + "," + c.getTickets() + ")";

                    st.executeUpdate(query);
                }

            }

            st.executeUpdate(query);

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cancelReservation(Reservation reservation) {

        reservations.remove(reservation);

        Connection con = null;
        Statement st = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "delete from reservations where realized=0 and id=" + reservation.getId();

            st.executeUpdate(query);

            query = "delete from sellinfo where resid=" + reservation.getId();

            st.executeUpdate(query);

            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void gatherMessagess() {

        messages.clear();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select * from messages where user='" + login.getUser().getUsername() + "'";

            rs = st.executeQuery(query);

            while (rs.next()) {

                Message message = new Message();

                message.setId(rs.getInt("id"));
                message.setUser(rs.getString("user"));
                message.setText(rs.getString("text"));

                messages.add(message);

            }
            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteMessage(Message message) {

        messages.remove(message);

        Connection con = null;
        Statement st = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "delete from messages where id='" + message.getId() + "'";

            st.executeUpdate(query);

            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<String> images() {
        List<String> images = new ArrayList<String>();
        Pattern patt = Pattern.compile("\\.(gif|jpe?g|png)$");

        if (displayEvent != null) {
            for (BelgradeEventFile af : displayEvent.getFiles()) {
                Matcher m = patt.matcher(af.getPath().toLowerCase());
                if (m.find()) {
                    images.add(af.getPath());

                }

            }
        }

        return images;
    }

    public void gatherCommentsData() {

        commentsEnabledEvents.clear();
        userComments.clear();

        for (Reservation r : boughtTickets) {
            if (r.getEventDate().before(new Date())) {
                commentsEnabledEvents.add(r);
            }
        }

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select * from commentsgrades where userid=" + login.getUser().getId();

            System.out.println("************" + login.getUser().getId());

            rs = st.executeQuery(query);

            while (rs.next()) {

                CommentGrade commentGrade = new CommentGrade();

                commentGrade.setId(rs.getInt("id"));
                commentGrade.setUserId(rs.getInt("userid"));
                commentGrade.setEventname(rs.getString("eventname"));
                commentGrade.setComment(rs.getString("comment"));
                commentGrade.setGrade(rs.getInt("grade"));

                userComments.add(commentGrade);

            }
            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void grading() {

        Connection con = null;
        Statement st = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            StringTokenizer strtok = new StringTokenizer(displayEventTitle, "-");

            String query = "insert into commentsgrades(userid, eventname, comment, grade) values(" + login.getUser().getId()
                    + ",'" + strtok.nextToken() + "','" + comment + "'," + grade + ")";

            st.executeUpdate(query);

            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        gatherCommentsData();

    }

    public void gatherReservationStatistics() {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        userStatistics.clear();
        userStatisticsDate.clear();

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select user, count(*) as cnt from reservations where realized=1 group by user order by cnt desc limit 10";

            rs = st.executeQuery(query);

            while (rs.next()) {

                StatisticElement statisticElement = new StatisticElement();

                statisticElement.setParam(rs.getString("user"));
                statisticElement.setStat(rs.getInt("cnt"));

                userStatistics.add(statisticElement);
            }

            query = "select user, count(*) as cnt from reservations where expirationdate > date_add(curdate(), interval -1 month)"
                    + " and realized=1 group by user order by cnt desc limit 10";

            rs = st.executeQuery(query);

            while (rs.next()) {

                StatisticElement statisticElement = new StatisticElement();

                statisticElement.setParam(rs.getString("user"));
                statisticElement.setStat(rs.getInt("cnt"));

                userStatisticsDate.add(statisticElement);

            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public boolean isSubmited() {
        return submited;
    }

    public void setSubmited(boolean submited) {
        this.submited = submited;
    }

    public List<BelgradeEventLocation> getEventsLocationList() {
        return eventsLocationList;
    }

    public void setEventsLocationList(List<BelgradeEventLocation> eventsLocationList) {
        this.eventsLocationList = eventsLocationList;
    }

    public List<String> getSelectedEventsLocationList() {
        return selectedEventsLocationList;
    }

    public void setSelectedEventsLocationList(List<String> selectedEventsLocationList) {
        this.selectedEventsLocationList = selectedEventsLocationList;
    }

    public List<BelgradeEvent> getSearchedEventsList() {
        return searchedEventsList;
    }

    public void setSearchedEventsList(List<BelgradeEvent> searchedEventsList) {
        this.searchedEventsList = searchedEventsList;
    }

    public String getDisplayEventTitle() {
        return displayEventTitle;
    }

    public void setDisplayEventTitle(String displayEventTitle) {
        this.displayEventTitle = displayEventTitle;
    }

    public BelgradeEvent getDisplayEvent() {
        return displayEvent;
    }

    public void setDisplayEvent(BelgradeEvent displayEvent) {
        this.displayEvent = displayEvent;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public int getTicketsNumber() {
        return ticketsNumber;
    }

    public void setTicketsNumber(int ticketsNumber) {
        this.ticketsNumber = ticketsNumber;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Timestamp getToday() {
        return today;
    }

    public void setToday(Timestamp today) {
        this.today = today;
    }

    public List<Reservation> getBoughtTickets() {
        return boughtTickets;
    }

    public void setBoughtTickets(List<Reservation> boughtTickets) {
        this.boughtTickets = boughtTickets;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Category> getEventCategories() {
        return eventCategories;
    }

    public void setEventCategories(List<Category> eventCategories) {
        this.eventCategories = eventCategories;
    }

    public List<CategortyAndCards> getCategoryTickets() {
        return categoryTickets;
    }

    public void setCategoryTickets(List<CategortyAndCards> categoryTickets) {
        this.categoryTickets = categoryTickets;
    }

    public List<Reservation> getCommentsEnabledEvents() {
        return commentsEnabledEvents;
    }

    public void setCommentsEnabledEvents(List<Reservation> commentsEnabledEvents) {
        this.commentsEnabledEvents = commentsEnabledEvents;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<CommentGrade> getUserComments() {
        return userComments;
    }

    public void setUserComments(List<CommentGrade> userComments) {
        this.userComments = userComments;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<CommentGrade> getEventCommentsGrades() {
        return eventCommentsGrades;
    }

    public void setEventCommentsGrades(List<CommentGrade> eventCommentsGrades) {
        this.eventCommentsGrades = eventCommentsGrades;
    }

    public List<StatisticElement> getUserStatistics() {
        return userStatistics;
    }

    public void setUserStatistics(List<StatisticElement> userStatistics) {
        this.userStatistics = userStatistics;
    }

    public List<StatisticElement> getUserStatisticsDate() {
        return userStatisticsDate;
    }

    public void setUserStatisticsDate(List<StatisticElement> userStatisticsDate) {
        this.userStatisticsDate = userStatisticsDate;
    }

}
