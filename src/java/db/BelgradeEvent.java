package db;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Dragan
 */

@Entity
@Table(name = "events")
public class BelgradeEvent implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false, unique=true)
    private int id = 0;
    
    private String title = "";
    
    @ManyToOne
    @JoinColumn(name = "locationid")
    private BelgradeEventLocation location;
    //private BelgradeEventLocation location = null;
    
    private Timestamp date = null;
    
    private boolean canceled = false;
    
    @Column(name = "soldtickets")
    private int soldTickets = 0;
    
    @Column(name = "resenable")
    private Timestamp reservationEnableDate = null;
    
    @Column(name = "maxres")
    private int maxReservations = 10;
    
    private String description = "";
    
    @OneToMany(fetch = FetchType.LAZY)
    private List<BelgradeEventFile> files = new LinkedList<>();
    
    @OneToMany(fetch = FetchType.LAZY)
    private List<Category> categories = new LinkedList<>();

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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    @Override
    public String toString() {
        return title;
    }
    
    
}
