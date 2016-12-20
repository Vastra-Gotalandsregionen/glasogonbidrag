package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.dto.ImportDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Migration;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.service.glasogonbidrag.domain.api.service.MigrationService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class MigrationServiceImpl implements MigrationService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MigrationServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void importData(long userId, long groupId, long companyId,
                           ImportDTO data) {
        if (hasMigrations()) {
            LOGGER.warn("Migrations already exists!");

            return;
        }

        Calendar cal = new GregorianCalendar();
        Date now = cal.getTime();

        for (Beneficiary beneficiary : data.getBeneficiaries()) {
            Migration migration = new Migration();
            migration.setDate(now);
            migration.setBeneficiary(beneficiary);

            beneficiary.setCreateDate(now);
            beneficiary.setModifiedDate(now);

            // Remove all prescriptions for this beneficiary.
            // It's the prescription that is the owing side.
            beneficiary.setPrescriptionHistory(
                    new HashSet<Prescription>());

            em.persist(beneficiary.getIdentification());
            em.persist(beneficiary);
            em.persist(migration);
        }

        for (Prescription prescription : data.getPrescriptions()) {
            Migration migration = new Migration();
            migration.setDate(now);
            migration.setPrescription(prescription);

            prescription.setUserId(userId);
            prescription.setGroupId(groupId);
            prescription.setCompanyId(companyId);
            prescription.setCreateDate(now);
            prescription.setModifiedDate(now);

            em.persist(prescription.getDiagnose());
            em.persist(prescription);
            em.persist(migration);
        }
    }

    @Override
    public boolean hasMigrations() {
        TypedQuery<Boolean> q = em.createNamedQuery(
                "glasogonbidrag.migration.exists", Boolean.class);

        try {
            return q.getSingleResult();
        } catch (NoResultException ignored) {
            return false;
        }
    }

    @Override
    public List<Migration> getMigrations() {
        TypedQuery<Migration> q = em.createNamedQuery(
                "glasogonbidrag.migration.findAll", Migration.class);

        return q.getResultList();
    }
}
