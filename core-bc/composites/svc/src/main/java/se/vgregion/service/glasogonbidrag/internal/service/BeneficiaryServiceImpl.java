package se.vgregion.service.glasogonbidrag.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.service.glasogonbidrag.api.service.BeneficiaryService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BeneficiaryServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void create(Beneficiary beneficiary) {
        LOGGER.info("Persisting beneficiary: {}", beneficiary);

        em.persist(beneficiary);
    }

    @Transactional
    public void update(Beneficiary beneficiary) {
        LOGGER.info("Updating beneficiary: {}", beneficiary);

        em.merge(beneficiary);
    }

    @Transactional
    public void delete(Long id) {
        Beneficiary beneficiary = em.find(Beneficiary.class, id);

        LOGGER.info("Deleting beneficiary: {}", beneficiary);

        em.remove(beneficiary);
    }
}
