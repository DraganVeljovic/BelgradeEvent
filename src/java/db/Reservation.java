package db;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Dragan
 */

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false, unique=true)
    private int id = 0;
    
    private String user = "";
    
    private int tickets = 0;
    
    @ManyToOne
    @JoinColumn(name = "eventid")
    private BelgradeEvent event;
    //private int eventid = 0;
    
    //private String eventTitle = "";
    //private Timestamp eventDate = null;
    
    @ManyToOne
    @JoinColumn(name = "locationid")
    private BelgradeEventLocation location;
    //private int locationid = 0;
    //private String locationTitle = "";
    
    @Column(name = "expirationdate")
    private Timestamp expirationDate = null;
    
    private boolean realized = false;
    
    private boolean adminapproval = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    /*
    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }
    */
    
    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isRealized() {
        return realized;
    }

    public void setRealized(boolean realized) {
        this.realized = realized;
    }

    /*
    public String getEventTitle() {
    return eventTitle;
    }
    public void setEventTitle(String eventTitle) {
    this.eventTitle = eventTitle;
    }
    public String getLocationTitle() {
    return locationTitle;
    }
    public void setLocationTitle(String locationTitle) {
    this.locationTitle = locationTitle;
    }
    public Timestamp getEventDate() {
    return eventDate;
    }
    public void setEventDate(Timestamp eventDate) {
    this.eventDate = eventDate;
    }
     */
    public BelgradeEvent getEvent() {
        return event;
    }

    public void setEvent(BelgradeEvent event) {
        this.event = event;
    }

    public BelgradeEventLocation getLocation() {
        return location;
    }

    public void setLocation(BelgradeEventLocation location) {
        this.location = location;
    }

    public boolean isAdminapproval() {
        return adminapproval;
    }

    public void setAdminapproval(boolean adminapproval) {
        this.adminapproval = adminapproval;
    }
    
    
    
    @Override
    public String toString() {
        return event.getTitle() + " - " + event.getLocation().getTitle() + " : " + event.getDate();
    }

    
    
}
