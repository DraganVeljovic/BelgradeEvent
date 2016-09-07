/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Drazen
 */
public class UserModel extends ListDataModel<User> implements SelectableDataModel<User> ,Serializable {
   
     public UserModel(){
         
     }
     
     public UserModel(List<User> data){
         super(data);
        
     }
    @Override
    public Object getRowKey(User t) {
       
          
        return t.getUsername();  
    }

    @Override
    public User getRowData(String string) {
        /*Session session = null;
        if(session==null) session=DbFactory.getSession();
        session.beginTransaction();
        Query q=session.createQuery("FROM User WHERE id=:id");
        q.setParameter("id", Integer.parseInt(string));
      
         List<User> results=q.list();
          session.getTransaction().commit();
        System.out.println(results.get(0).getUsername());
        return results.get(0);*/
        
         List<User> users = (List<User>) getWrappedData();  
          
        for(User u : users) {  
            if(u.getUsername().equals(string))  
                return u;  
        }  
          
        return null; 
        
    }
    
}
