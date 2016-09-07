
package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Dragan
 */
@ManagedBean
@SessionScoped
public class TicketsControl {

    private String selectedEventTitle = "";
    
    @ManagedProperty(value="#{eventControl}")
    private EventControl eventControl;
    
    private int ticketsToSell = 0;
    
    private List<Reservation> reservations = new LinkedList<>();
    
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
                    + " and r.locationid=l.id and r.locationid=" + eventControl.getLocationId() + " and r.realized=0";
            
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
                reservation.setExpirationDate(rs.getTimestamp("expirationDate"));
                reservation.setRealized(rs.getBoolean("realized"));
                
                reservations.add(reservation);
                
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void sellTickets() {
        
        Connection con = null;
        Statement st = null;
        
        BelgradeEvent selectedEvent = eventControl.getAllEventsTitles().get(selectedEventTitle);
       
        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();
            
            String query = "update events set soldtickets=" + (ticketsToSell + selectedEvent.getSoldTickets()) 
                    + " where id=" + selectedEvent.getId();
            
            selectedEvent.setSoldTickets(selectedEvent.getSoldTickets() + ticketsToSell);
            
            st.executeUpdate(query);
            
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("Prodaja " + ticketsToSell + " karata je uspesna!"));
            
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void sellTickets(Reservation reservation) {
        
        reservations.remove(reservation);
        
        Connection con = null;
        Statement st = null;
       
        BelgradeEvent selectedEvent = eventControl.getAllEventsTitles().get(reservation.getEventTitle());
        
        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "update reservations set realized=1 where id=" + reservation.getId();
            
            st.executeUpdate(query);
            
            String text = "Uspesno ste kupili " + reservation.getTickets() + " ulaznica za dogadjaj: " + reservation.getEventTitle() + 
                    " koji se odrzava " + reservation.getEventDate() + " u " + reservation.getLocationTitle() + 
                    ". \nHvala na poverenju! \nBelgradeEvent Tim." ;

            query = "insert into messages(user, text) values('" + reservation.getUser() + "', '"
                    + text + "')";
            
            st.executeUpdate(query);

            query = "update events set soldtickets=" + (reservation.getTickets() + selectedEvent.getSoldTickets())
                    + " where id=" + reservation.getEventid();
            
            st.executeUpdate(query);
            
            selectedEvent.setSoldTickets(selectedEvent.getSoldTickets() + ticketsToSell);
            
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventControl getEventControl() {
        return eventControl;
    }

    public void setEventControl(EventControl eventControl) {
        this.eventControl = eventControl;
    }

    public int getTicketsToSell() {
        return ticketsToSell;
    }

    public void setTicketsToSell(int ticketsToSell) {
        this.ticketsToSell = ticketsToSell;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public String getSelectedEventTitle() {
        return selectedEventTitle;
    }

    public void setSelectedEventTitle(String selectedEventTitle) {
        this.selectedEventTitle = selectedEventTitle;
    }
    
    
}
