package se.vgregion.service.glasogonbidrag.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.service.glasogonbidrag.api.service.BeneficiaryService;
import se.vgregion.service.glasogonbidrag.exception.NoIdentificationException;

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
}
