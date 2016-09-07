package beans;

import java.io.Serializable;

/**
 *
 * @author Dragan
 */
public class BelgradeEventLocation implements Serializable {
    
    private int id = 0;
    
    private String title = "";
    private String address = "";
    
    private int capacity = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return title;
    }
    
    
}
