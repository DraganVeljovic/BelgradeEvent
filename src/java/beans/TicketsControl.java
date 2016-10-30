
package beans;

import db.BelgradeEvent;
import db.DbFactory;
import db.Message;
import db.Reservation;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;

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
    
    private Session session;
    
    public void gatherReservationData() {
        
        reservations.clear();
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        Query query = session.createQuery("from Reservation");
        
        reservations = query.list();
        
        session.close();
        
    }
    
    public void sellTickets() {
        
        BelgradeEvent selectedEvent = eventControl.getAllEventsTitles().get(selectedEventTitle);

        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        selectedEvent.setSoldTickets(selectedEvent.getSoldTickets() + ticketsToSell);
            
        session.update(selectedEvent);
        
        session.getTransaction().commit();
        
        session.close();
        
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("Prodaja " + ticketsToSell + " karata je uspesna!"));
         
    }
    
    public void sellTickets(Reservation reservation) {
        
        reservations.remove(reservation);
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        reservation.setRealized(true);
        
        reservation.getEvent().setSoldTickets(reservation.getTickets() + reservation.getEvent().getSoldTickets());
        
        session.update(reservation);
            
        String text = "Uspesno ste kupili " + reservation.getTickets() + " ulaznica za dogadjaj: " + reservation.getEvent().getTitle() + 
                    " koji se odrzava " + reservation.getEvent().getDate() + " u " + reservation.getLocation().getTitle() + 
                    ". \nHvala na poverenju! \nBelgradeEvent Tim." ;

        Message message = new Message();
        
        message.setText(text);
        message.setUser(reservation.getUser());
        
        session.save(message);
        
        session.getTransaction().commit();
        
        session.close();
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
