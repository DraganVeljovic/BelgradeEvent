package beans;

import model.LocationModel;
import db.User;
import db.BelgradeEventLocation;
import static beans.Registration.isValidEmailAddress;
import db.Cashier;
import db.DbFactory;
import db.Reservation;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import model.UserModel;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Dragan
 */
@ManagedBean
@SessionScoped
public class UserControl implements Serializable {

    private List<User> waitingForApproval = new LinkedList<>();
    private List<User> blocked = new LinkedList<>();
    private List<User> allUsers = new LinkedList<>();
    
    private List<BelgradeEventLocation> locations = new LinkedList<>();
    
    private UserModel userModel;
    private LocationModel locationModel;
    
    private User selectedUser = new User();
  
    private User newUser = new User();
    
    private boolean isCashier = false;
    
    private BelgradeEventLocation selectedLocation = new BelgradeEventLocation();
    
    private BelgradeEventLocation newLocation = new BelgradeEventLocation();
    
    private Session session = null;
    
    private String selectedItem = "";
    private HashMap<String, BelgradeEventLocation> mapedLocations = new HashMap<>();
    private List<SelectItem> selectedItems = new LinkedList<>();
    
    public void gatherApprovalData() {
        
        waitingForApproval.clear();
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        Query query = session.createQuery("from User where status=0");
        
        List<User> users = query.list();
        
        for (User u : users) {
            waitingForApproval.add(u);
        }
        
        session.close();
        
        userModel = new UserModel(waitingForApproval);
        
    }
    
    public void gatherBlockedData() {
        
        blocked.clear();
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        Query query = session.createQuery("from User where blocked=1");
        
        List<User> users = query.list();
        
        for (User u : users) {
            blocked.add(u);
        }
        
        userModel = new UserModel(blocked);
        
        session.close();
    }
    
    public void gatherAllUsersData() {

        allUsers.clear();
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        Query query = session.createQuery("from User");
        
        List<User> users = query.list();
        
        for (User u : users) {
            allUsers.add(u);
        }
        
        userModel = new UserModel(allUsers);
        
        session.close();

    }
    
    public void gatherEventLocations() {
        
        locations.clear();
        mapedLocations.clear();
        selectedItems.clear();
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        Query query = session.createQuery("from BelgradeEventLocation");
        
        List<BelgradeEventLocation> eventLocations = query.list();
        
        for (BelgradeEventLocation bel : eventLocations) {
            locations.add(bel);
        
            mapedLocations.put(bel.getTitle(), bel);
            selectedItems.add(new SelectItem(bel, bel.getTitle()));
        }
        
        if (!selectedItems.isEmpty()) {
            selectedItem = selectedItems.get(0).getLabel();
            selectedLocation = (BelgradeEventLocation) selectedItems.get(0).getValue();
        } 
        
        session.close();
        
        locationModel = new LocationModel(locations);
    
    }

    public void approveSelectedUser() {

        waitingForApproval.remove(selectedUser);

        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        selectedUser.setStatus(true);
        
        session.update(selectedUser);
        
        session.getTransaction().commit();
        
        userModel = new UserModel(waitingForApproval);
        
        session.close();
    }

    public void deleteSelectedUser(int type) {
    
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        session.delete(selectedUser);
        
        session.getTransaction().commit();
        
        session.close();
        
        gatherAllUsersData();
        gatherApprovalData();
        gatherBlockedData();
        
        switch (type) {
            case 0: userModel = new UserModel(waitingForApproval);
                    break;
            case 1: userModel = new UserModel(blocked);
                    break;
            default:
                    userModel = new UserModel(allUsers);
                    break;
        }
        
    }
    
    public void deblockSelectedUser() {
        
        blocked.remove(selectedUser);
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        selectedUser.setBlocked(false);
        
        session.update(selectedUser);
        
        Query query = session.createQuery("from Reservation where user=:u and realized=0 and expirationdate < curdate()");
        query.setParameter("u", selectedUser.getUsername());
        
        List<Reservation> res = query.list();
        
        for (Reservation r : res) {
            r.setAdminapproval(true);
            
            session.update(r);
        }
        
        session.getTransaction().commit();
        
        session.close();

        userModel = new UserModel(blocked);
    }
    
    public void updateSelectedUserData() {
 
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        session.update(selectedUser);
        
        session.getTransaction().commit();
        
        session.close();
        
        gatherAllUsersData();
 
    }
    
    public String addUser() {
        if (!isValidEmailAddress(newUser.getEmail())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email adresa nije ispravna", ""));
            return null;
        }
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        Query query = session.createQuery("from User where username=:u or email=:e");
        query.setParameter("u", newUser.getUsername());
        query.setParameter("e", newUser.getEmail());
        
        List<User> users = query.list();
        
        if (!users.isEmpty()) {
            
            session.close();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Korisnicko ime i/ili email vec postoje"));
            
            return null;
        }
        
        newUser.setStatus(true);
        
        session.save(newUser);
        
        
        if (newUser.getType() == 1) {
            
            Cashier c = new Cashier();
            
            c.setUser(newUser);
            c.setLocation(selectedLocation);
            
            session.save(c);
        }
        
        session.getTransaction().commit();
        
        session.close();
        
        return "usersOverview?faces-redirect=true";

    }
    
    public void addLocation() {
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        Query query = session.createQuery("from BelgradeEventLocation where title=:t");
        query.setParameter("t", newLocation.getTitle());
        
        List<BelgradeEventLocation> eventLocations = query.list();
        
        if (!eventLocations.isEmpty()) {
        
            session.close();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Lokacija vec postoji"));
            
        } else {
            
            session.save(newLocation);
            
            session.getTransaction().commit();
        
            session.close();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Lokacija je uspesno dodata!"));
        }
    }
    
    public void updateSelectedLocationData() {
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        session.update(selectedLocation);
        
        session.getTransaction().commit();
        
        session.close();
        
        gatherEventLocations();
    }
    
    public void deleteSelectedLocation() {
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        session.delete(selectedLocation);
        
        session.getTransaction().commit();
        
        session.close();
        
        gatherEventLocations();
    
    }
    
    public void onUserTypeChange(AjaxBehaviorEvent  e) {
        
        if (newUser.getType() == 1) {
            System.out.println("**** Blagajnik ****   " + isCashier);
            isCashier = true;
            System.out.println("**** Blagajnik promenjen ****   " + isCashier);
        }
    }

    public List<User> getWaitingForApproval() {
        return waitingForApproval;
    }

    public void setWaitingForApproval(List<User> waitingForApproval) {
        this.waitingForApproval = waitingForApproval;
    }

    public List<User> getBlocked() {
        return blocked;
    }

    public void setBlocked(List<User> blocked) {
        this.blocked = blocked;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public List<BelgradeEventLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<BelgradeEventLocation> locations) {
        this.locations = locations;
    }

    public BelgradeEventLocation getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(BelgradeEventLocation selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public BelgradeEventLocation getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(BelgradeEventLocation newLocation) {
        this.newLocation = newLocation;
    }

    public boolean isIsCashier() {
        return isCashier;
    }

    public void setIsCashier(boolean isCashier) {
        this.isCashier = isCashier;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public LocationModel getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
        setSelectedLocation(mapedLocations.get(selectedItem));
    }

    public HashMap<String, BelgradeEventLocation> getMapedLocations() {
        return mapedLocations;
    }

    public void setMapedLocations(HashMap<String, BelgradeEventLocation> mapedLocations) {
        this.mapedLocations = mapedLocations;
    }

    public List<SelectItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<SelectItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    
}
