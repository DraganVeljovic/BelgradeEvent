/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/**
 *
 * @author Drazen
 */

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;



public class DbFactory {
   private static SessionFactory factory;
   private static ServiceRegistry serviceRegistry;
  static {
        try {
    
        Configuration configuration = new Configuration();
        configuration.configure();

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        factory = configuration.buildSessionFactory(serviceRegistry);
       
       } catch (Throwable ex) {

            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
   }
   public static Session getSession(){
       
        Session session = factory.getCurrentSession();
       
       return session;
       
   }
   
   public static SessionFactory getFactory(){
       
       return factory;
   }

}
