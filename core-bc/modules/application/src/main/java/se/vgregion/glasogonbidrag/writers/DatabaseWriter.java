package se.vgregion.glasogonbidrag.writers;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.PersonalIdentification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class DatabaseWriter {

    private String host;
    private String db;
    private String username;
    private String password;

    public DatabaseWriter() {
        this("localhost:5432", "lportal_vgr", "lportal", "password");
    }

    public DatabaseWriter(String host, String db,
                          String username, String password) {
        this.host = host;
        this.db = db;
        this.username = username;
        this.password = password;
    }

    public void run() {
        Map<String, Object> override = new HashMap<>();

        // the fully qualified class name of the driver class
        override.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        // the driver specific URL
        override.put(
                "javax.persistence.jdbc.url",
                String.format("jdbc:postgresql://%s/%s", host, db));
        // the user name used for the database connection
        override.put("javax.persistence.jdbc.user", username);
        // the password used for the database connection
        override.put("javax.persistence.jdbc.password", password);

        // Don't re-create or modify the schema.
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
