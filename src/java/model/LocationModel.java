/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import db.BelgradeEventLocation;
import db.User;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Drazen
 */
public class LocationModel extends ListDataModel<BelgradeEventLocation> implements SelectableDataModel<BelgradeEventLocation> ,Serializable {
   
     public LocationModel(){
         
     }
     
     public LocationModel(List<BelgradeEventLocation> data){
         super(data);
        
     }
    @Override
    public Object getRowKey(BelgradeEventLocation t) {
       
          
        return t.getTitle();  
    }

    @Override
    public BelgradeEventLocation getRowData(String string) {
        /*Session session = null;
        if(session==null) session=DbFactory.getSession();
        session.beginTransaction();
        Query q=session.createQuery("FROM User WHERE id=:id");
        q.setParameter("id", Integer.parseInt(string));
      
         List<User> results=q.list();
          session.getTransaction().commit();
        System.out.println(results.get(0).getUsername());
        return results.get(0);*/
        
         List<BelgradeEventLocation> locations = (List<BelgradeEventLocation>) getWrappedData();  
          
        for(BelgradeEventLocation l : locations) {  
            if(l.getTitle().equals(string))  
                return l;  
        }  
          
        return null; 
        
    }
    
}
