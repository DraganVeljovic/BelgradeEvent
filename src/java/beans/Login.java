/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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

    public String checkLogin() throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();
            String query = "select * from users where username='" + user.getUsername() + "' and password='" + user.getPassword() + "'";

            rs = st.executeQuery(query);

            if (rs.next()) {

                user.setId(rs.getInt("id"));
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

                Timestamp loginDate = new Timestamp((new Date()).getTime());

                user.setRegistered(rs.getTimestamp("registered"));

                if (!user.isStatus()) {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Administrator Vam jos uvek nije omogucio pristup!"));

                } else {

                    if (user.isBlocked()) {

                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Blokirani ste zbog tri nerealizovane rezervacije!"));

                    } else {

                        query = "select count(*) from reservations where realized=0 and adminapproval=0 "
                                + "and user='" + user.getUsername() + "' and expirationdate<=curdate()";

                        ResultSet rs2 = st.executeQuery(query);

                        if (rs2.next()) {
                            if (rs2.getInt(1) >= 3) {

                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Blokirani ste zbog tri nerealizovane rezervacije!"));

                                query = "update users set blocked=1 where username='" + user.getUsername() + "'";

                                st.executeUpdate(query);

                                return null;

                            }
                        }

                        rs2.close();
                        
                        user.setLastlogin(loginDate);
                
                        user.setLogged(true);

                        query = "update users set lastlogin='" + loginDate + "' where username='" + user.getUsername() + "'";

                        st.executeUpdate(query);

                        return "index?faces-redirect=true";

                    }

                }
            } else {

                context.addMessage(null, new FacesMessage("Neispravni podaci!"));
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }

    public String changeLoginData() {

        FacesContext context = FacesContext.getCurrentInstance();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/piaprojekat", "root", "");

            st = con.createStatement();
            String query = "select * from users where username='" + user.getUsername() + "' and password='" + user.getPassword() + "'";

            rs = st.executeQuery(query);

            if (rs.next()) {

                if (!rs.getBoolean("status")) {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Administrator Vam jos uvek nije omogucio pristup"));

                } else {

                    query = "update users set password='" + user.getPassword2() + "', password2='" + user.getPassword2()
                            + "' where username='" + user.getUsername() + "'";

                    st.executeUpdate(query);

                    return "index?faces-redirect=true";

                }
            } else {

                context.addMessage(null, new FacesMessage("Neispravni podaci!"));
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/faces/index?faces-redirect=true";
    }

    public void changeData() {

    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

}
