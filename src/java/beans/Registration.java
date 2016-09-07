/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author Drazen
 */
@ManagedBean
@SessionScoped
public class Registration implements Serializable {

    private User user = new User();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String register() {

        if (!isValidEmailAddress(user.getEmail())) {
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
            
            String query = "select * from users where username='" + user.getUsername() + "' or email='" + user.getEmail() + "'";
            
            rs = st.executeQuery(query);
            
            if (!rs.next()) {
            
                query = "insert into users(username, password, password2, firstname, lastname, email, telephone, address,"
                        + " city, type, status, blocked, lastlogin, registered) "
                        + "values('" + user.getUsername() + "','" + user.getPassword() + "','" + user.getPassword2() + "','"
                        + user.getFirstName() + "','" + user.getLastName() + "','" + user.getEmail() + "','" + user.getTelephone() + "','"
                        + user.getAddress() + "','" + user.getCity() + "'," + user.getType() + "," + user.isStatus() + ","
                        + user.isBlocked() + ",'" + user.getLastlogin() + "','" + user.getRegistered() + "')";

                st.executeUpdate(query);
                
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

        return "registrationApproval?faces-redirect=true";

    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
