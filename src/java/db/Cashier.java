package db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Dragan
 */
@Entity
@Table(name="cashiers")
public class Cashier implements Serializable {
    
    @Id
    @Column(nullable = false, unique = true)
    private int id;
    
    @OneToOne
    @JoinColumn(name = "userid")
    private User user;
    //private int userid = 0;
    
    @OneToOne
    @JoinColumn(name = "locationid")
    private BelgradeEventLocation location;

    //private int locationid = 0;
    /*
    public int getUserid() {
    return userid;
    }
    public void setUserid(int userid) {
    this.userid = userid;
    }
    public int getLocationid() {
    return locationid;
    }
    public void setLocationid(int locationid) {
    this.locationid = locationid;
    }
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BelgradeEventLocation getLocation() {
        return location;
    }

    public void setLocation(BelgradeEventLocation location) {
        this.location = location;
    }
    
    
    
}
