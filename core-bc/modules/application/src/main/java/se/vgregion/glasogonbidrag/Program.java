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
 * @author Martin Lind - Monator Technologies AB
 */
public class Program {
    public static void main(String[] args) throws Exception {
        ExcelImporter importer = new ExcelImporter();
        importer.benficiaryRun();
    }
}
