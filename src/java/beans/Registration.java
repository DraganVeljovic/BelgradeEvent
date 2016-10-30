/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.DbFactory;
import db.User;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Drazen
 */
@ManagedBean
@SessionScoped
public class Registration implements Serializable {

    private User user = new User();

    private Session session = null;

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

        session = DbFactory.getFactory().openSession();

        session.beginTransaction();

        Query query = session.createQuery("from User where username=:u or email=:e");
        query.setParameter("u", user.getUsername());
        query.setParameter("e", user.getEmail());

        List<User> users = query.list();

        if (users.isEmpty()) {
            
            session.save(user);

            session.getTransaction().commit();

            session.close();

            return "registrationApproval?faces-redirect=true";

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Korisnicko ime i/ili email vec postoje"));

        }

        session.close();

        return null;

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
