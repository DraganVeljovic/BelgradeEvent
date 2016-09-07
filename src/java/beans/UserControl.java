package beans;

import static beans.Registration.isValidEmailAddress;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

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
    
    private UserModel model;
    
    private User selectedUser = new User();
  
    private User newUser = new User();
    
    private boolean isCashier = false;
    
    private BelgradeEventLocation selectedLocation = new BelgradeEventLocation();
    
    private BelgradeEventLocation newLocation = new BelgradeEventLocation();
    
    public void gatherApprovalData() {
        
        waitingForApproval.clear();
        
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select * from users where status=0";
            
            rs = st.executeQuery(query);

            while (rs.next()) {

                User user = new User();

                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPassword2(rs.getString("password2"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setTelephone(rs.getString("telephone"));
                user.setAddress(rs.getString("address"));
                user.setCity(rs.getString("city"));
                user.setType(rs.getInt("type"));
                user.setStatus(rs.getBoolean("status"));
                user.setBlocked(rs.getBoolean("blocked"));
                user.setLastlogin(rs.getTimestamp("lastlogin"));
                user.setRegistered(rs.getTimestamp("registered"));

                waitingForApproval.add(user);
                
                model = new UserModel(waitingForApproval);
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    public void gatherBlockedData() {
        
        blocked.clear();
        
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select * from users where blocked=1";
            rs = st.executeQuery(query);

            while (rs.next()) {

                User user = new User();

                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPassword2(rs.getString("password2"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setTelephone(rs.getString("telephone"));
                user.setAddress(rs.getString("address"));
                user.setCity(rs.getString("city"));
                user.setType(rs.getInt("type"));
                user.setStatus(rs.getBoolean("status"));
                user.setBlocked(rs.getBoolean("blocked"));
                user.setLastlogin(rs.getTimestamp("lastlogin"));
                user.setRegistered(rs.getTimestamp("registered"));

                blocked.add(user);
                
                model = new UserModel(blocked);
                
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void gatherAllUsersData() {

        allUsers.clear();
        
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "select * from users";
            rs = st.executeQuery(query);

            while (rs.next()) {

                User user = new User();

                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPassword2(rs.getString("password2"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setTelephone(rs.getString("telephone"));
                user.setAddress(rs.getString("address"));
                user.setCity(rs.getString("city"));
                user.setType(rs.getInt("type"));
                user.setStatus(rs.getBoolean("status"));
                user.setBlocked(rs.getBoolean("blocked"));
                user.setLastlogin(rs.getTimestamp("lastlogin"));
                user.setRegistered(rs.getTimestamp("registered"));

                allUsers.add(user);
                
                model = new UserModel(allUsers);
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void gatherEventLocations() {
        
        locations.clear();
        
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

                BelgradeEventLocation location = new BelgradeEventLocation();

                location.setTitle(rs.getString("title"));
                location.setAddress(rs.getString("address"));
                location.setCapacity(rs.getInt("capacity"));
                
                locations.add(location);
                
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

    public void approveUser(User user) {

        waitingForApproval.remove(user);

        Connection con = null;
        Statement st = null;
       
        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "update users set status=1 where username='" + user.getUsername() + "'";
          
            st.executeUpdate(query);
            
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteUser(User user) {
        
        waitingForApproval.remove(user);
        
        Connection con = null;
        Statement st = null;
       
        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "delete from users where username='" + user.getUsername() + "'";
          
            st.executeUpdate(query);
            
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    public void deleteSelectedUser() {
    
        Connection con = null;
        Statement st = null;
       
        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "delete from users where username='" + selectedUser.getUsername() + "'";
          
            st.executeUpdate(query);
            
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    public void deblock(User user) {
        
        blocked.remove(user);

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
       
        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "update users set blocked=0 where username='" + user.getUsername() + "'";
          
            st.executeUpdate(query);
            
            query = "update reservations set adminapproval=1, realized=1 where realized=0 and adminapproval=0 and user='"
                    + user.getUsername() + "'";
            
            st.executeUpdate(query);
            
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void updateUserData() {
 
        Connection con = null;
        Statement st = null;
       
        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();

            String query = "update users set type='" + selectedUser.getType() + "'";
          
            st.executeUpdate(query);
            
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
    
    public String addUser() {
        if (!isValidEmailAddress(newUser.getEmail())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email adresa nije ispravna", ""));
            return null;
        }

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();
            
            String query = "select * from users where username='" + newUser.getUsername() + "' or email='" + newUser.getEmail() + "'";
            
            rs = st.executeQuery(query);
            
            if (!rs.next()) {
            
                query = "insert into users(username, password, password2, firstname, lastname, email, telephone, address,"
                        + " city, type, status, blocked, lastlogin, registered) "
                        + "values('" + newUser.getUsername() + "','" + newUser.getPassword() + "','" + newUser.getPassword2() + "','"
                        + newUser.getFirstName() + "','" + newUser.getLastName() + "','" + newUser.getEmail() + "','" + newUser.getTelephone() + "','"
                        + newUser.getAddress() + "','" + newUser.getCity() + "'," + newUser.getType() + "," + true + ","
                        + false + ",'" + newUser.getLastlogin() + "','" + newUser.getRegistered() + "')";

                st.executeUpdate(query);
                
                if (newUser.getType() == 1) {
                    
                    ResultSet rs2 = null;
                    
                    query = "select id from users where username='" + newUser.getUsername() + "'";
                    
                    rs2 = st.executeQuery(query);
                    
                    if (rs2.next()) {
                        
                        ResultSet rs3 = null;
                        
                        int userid = rs2.getInt("id");
                        
                        query = "select id from locations where title='" + selectedLocation.getTitle() + "'";
                        
                        rs3 = st.executeQuery(query);
                        
                        if (rs3.next()) {
                         
                            int locationid = rs3.getInt("id");
                            
                            query = "insert into cachier(userid, locationid) values(" + userid + "," + locationid + ")";
                        
                            st.executeUpdate(query);
                            
                        }
                        
                        rs3.close();
                        
                    }
                    
                    rs2.close();
                    
                }
                
                
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Korisnicko ime i/ili email vec postoje"));
                return null;
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
        
        return "usersOverview?faces-redirect=true";

    }
    
    public void addLocation() {
        
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();
            
            String query = "select * from locations where title='" + newLocation.getTitle() + "'";
            
            rs = st.executeQuery(query);
            
            if (!rs.next()) {
            
                query = "insert into locations(title, address, capacity) "
                        + "values('" + newLocation.getTitle() + "','" + newLocation.getAddress() + "'," 
                        + newLocation.getCapacity() + ")";

                st.executeUpdate(query);
                
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Lokacija vec postoji"));
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
        
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

    public UserModel getModel() {
        return model;
    }

    public void setModel(UserModel model) {
        this.model = model;
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

    
    
}
