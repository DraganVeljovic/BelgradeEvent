package db;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author Dragan
 */
public class SellInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false, unique=true)
    private int id = 0;
    
    @OneToOne
    @JoinColumn(name = "resid")
    private Reservation reservation;
    //private int resid = 0;
    
    @OneToOne
    @JoinColumn(name = "catid")
    private Category category;
    //private int catid = 0;
    
    private int amount = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*
    public int getResid() {
    return resid;
    }
    public void setResid(int resid) {
    this.resid = resid;
    }
    public int getCatid() {
    return catid;
    }
    public void setCatid(int catid) {
    this.catid = catid;
    }
     */
    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    
    
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    
    
}
