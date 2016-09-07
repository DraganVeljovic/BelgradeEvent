package beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dragan
 */
public class BelgradeEvent implements Serializable {
    
    private int id = 0;
    
    private String title = "";
    
    private BelgradeEventLocation location = null;
    
    private Timestamp date = null;
    
    private int soldTickets = 0;
    
    private boolean canceled = false;
    
    private Timestamp reservationEnableDate = null;
    
    private int maxReservations = 10;
    
    private String description = "";
    
    private List<BelgradeEventFile> files = new LinkedList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BelgradeEventLocation getLocation() {
        return location;
    }

    public void setLocation(BelgradeEventLocation location) {
        this.location = location;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }

    public Timestamp getReservationEnableDate() {
        return reservationEnableDate;
    }

    public void setReservationEnableDate(Timestamp reservationEnableDate) {
        this.reservationEnableDate = reservationEnableDate;
    }

    public int getMaxReservations() {
        return maxReservations;
    }

    public void setMaxReservations(int maxReservations) {
        this.maxReservations = maxReservations;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BelgradeEventFile> getFiles() {
        return files;
    }

    public void setFiles(List<BelgradeEventFile> files) {
        this.files = files;
    }
    
    
    
    @Override
    public String toString() {
        return title;
    }
    
    
}
