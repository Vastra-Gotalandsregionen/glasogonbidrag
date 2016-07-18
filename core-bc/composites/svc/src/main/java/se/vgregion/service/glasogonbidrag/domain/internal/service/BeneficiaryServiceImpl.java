package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.service.glasogonbidrag.domain.api.service.BeneficiaryService;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BeneficiaryServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional(rollbackFor = { NoIdentificationException.class })
    public void create(Beneficiary beneficiary)
            throws NoIdentificationException {

        // Require that identification is set.
        Identification identification = beneficiary.getIdentification();

        if (identification == null) {
            throw new NoIdentificationException("Identification is not set.");
        }

        LOGGER.info("Persisting identification: {}", identification);
        em.persist(identification);

        // Update creation date and modification date
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        beneficiary.setCreateDate(date);
        beneficiary.setModifiedDate(date);

        LOGGER.info("Persisting beneficiary: {}", beneficiary);
        em.persist(beneficiary);
    }

    @Transactional(rollbackFor = { NoIdentificationException.class })
    public void update(Beneficiary beneficiary)
            throws NoIdentificationException {
        // Require that identification is set.
        Identification identification = beneficiary.getIdentification();

        if (identification == null) {
            throw new NoIdentificationException("Identification is not set.");
        }

        LOGGER.info("Updating identification: {}", identification);
        em.merge(identification);

        // Update modification date
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        beneficiary.setModifiedDate(date);

        LOGGER.info("Updating beneficiary: {}", beneficiary);
        em.merge(beneficiary);
    }

    @Transactional
    public void delete(Long id) {
        Beneficiary beneficiary = em.find(Beneficiary.class, id);

        Identification identification = beneficiary.getIdentification();

        LOGGER.info("Deleting beneficiary: {}", beneficiary);
        em.remove(beneficiary);

        LOGGER.info("Deleting identification: {}", identification);
        em.remove(identification);
    }

    @Override
    @Transactional
    public void updateAddPrescription(
            long userId, long groupId, long companyId,
            Beneficiary beneficiary, Prescription prescription) {
        LOGGER.info("Add prescription: {} to beneficiary {}.",
                prescription, beneficiary);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        // Set user, group and company id of new prescription.
        prescription.setUserId(userId);
        prescription.setGroupId(groupId);
        prescription.setCompanyId(companyId);

        // Set creation date and modification date of new prescription
        prescription.setCreateDate(date);
        prescription.setModifiedDate(date);

        // Set relation from prescription to beneficiary
        prescription.setBeneficiary(beneficiary);
        // Add prescription to beneficiary history
        beneficiary.getPrescriptionHistory().add(prescription);

        em.persist(prescription);

        try {
            update(beneficiary);
        } catch (NoIdentificationException e) {
            throw new RuntimeException(e);
        }
    }
}
