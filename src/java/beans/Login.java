/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.DbFactory;
import db.User;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Drazen
 */
@ManagedBean
@SessionScoped
public class Login implements Serializable {

    private User user = new User();
    private String tempPass = "";
    private String tempPass2 = "";
    private boolean changePassword = false;
    
    private Session session = null;

    public String checkLogin() throws IOException {

        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        Query query = session.createQuery("from User where username=:u and password=:p");
        query.setParameter("u", user.getUsername());
        query.setParameter("p", user.getPassword());
        
        List<User> users = query.list();
        
        if (!users.isEmpty()) {
            
            user = users.get(0);
            
            if (!user.isStatus()){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Administrator Vam jos uvek nije omogucio pristup!"));
            } else {
                if (user.isBlocked()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Blokirani ste zbog tri nerealizovane rezervacije!"));
                } else {
                    String sqlQuery = "select count(*) from reservations where realized=0 and adminapproval=0 "
                                + "and user='" + user.getUsername() + "' and expirationdate<=curdate()";
                    
                    query = session.createSQLQuery(sqlQuery);
                    
                    List<java.math.BigInteger> counts = query.list();
                    
                    if (!counts.isEmpty()) {
                        
                        if (counts.get(0).intValue() >= 3) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Blokirani ste zbog tri nerealizovane rezervacije!"));

                            user.setBlocked(true);
                            
                            session.update(user);
                            
                            session.getTransaction().commit();
                            
                            session.close();

                            return null;
                        }
                        
                    }
                    
                    user.setLastlogin(new Timestamp(new Date().getTime()));
                    user.setLogged(true);
                    
                    session.update(user);
                    
                    session.getTransaction().commit();
                    
                    session.close();
                    
                    return "index?faces-redirect=true";
                    
                }
            }
            
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Neispravni podaci!"));
        }
        
        session.close();
        
        return null;
    }

    public String changeLoginData() {
        
        session = DbFactory.getFactory().openSession();
        
        session.beginTransaction();
        
        Query query = session.createQuery("from User where username=:u and password=:p");
        query.setParameter("u", user.getUsername());
        query.setParameter("p", user.getPassword());

        List<User> users = query.list();
        
        if (!users.isEmpty()) {
             
            User user = users.get(0);
            
            if (!user.isStatus()) {
                
                session.close();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Administrator Vam jos uvek nije omogucio pristup"));

            } else {
                
                user.setPassword(this.user.getPassword2());
                user.setPassword2(this.user.getPassword2());
                
                this.user = user;
                
                session.update(this.user);
                
                session.getTransaction().commit();
                session.close();
                
                return "index?faces-redirect=true";
            }
            
        } else {
            
            session.close();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Neispravni podaci!"));
        }
        
        return null;
        
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/faces/index?faces-redirect=true";
    }

    public void changeData() {

    }

    public String getTempPass() {
        return tempPass;
    }

    public void setTempPass(String tempPass) {
        this.tempPass = tempPass;
    }

    public String getTempPass2() {
        return tempPass2;
    }

    public void setTempPass2(String tempPass2) {
        this.tempPass2 = tempPass2;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

}
