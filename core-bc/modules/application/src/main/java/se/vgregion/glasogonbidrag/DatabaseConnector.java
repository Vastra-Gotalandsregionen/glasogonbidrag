package se.vgregion.glasogonbidrag;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.PersonalIdentification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martlin on 2016/06/14.
 */
public class DatabaseConnector {
    public void run() {
        Map<String, Object> override = new HashMap<>();

        // the fully qualified class name of the driver class
        override.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        // the driver specific URL
        override.put(
                "javax.persistence.jdbc.url",
                "jdbc:postgresql://localhost:5432/lportal_vgr");
        // the user name used for the database connection
        override.put("javax.persistence.jdbc.user", "lportal");
        // the password used for the database connection
        override.put("javax.persistence.jdbc.password", "password");

        override.put("hibernate.hbm2ddl.auto", "none");


        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory(
                        "glasogonbidrag", override);

        EntityManager em = factory.createEntityManager();



        Identification id = new PersonalIdentification();
        ((PersonalIdentification)id).setNumber("880418-4714");

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            em.persist(id);

            tx.commit();
        } catch (RuntimeException e) {
            if ( tx != null && tx.isActive() ) tx.rollback();
            throw e; // or display error message
        } finally {
            em.close();
            factory.close();
        }
    }
}
