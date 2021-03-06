package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.glasogonbidrag.domain.dto.BeneficiaryDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.service.glasogonbidrag.domain.api.service.BeneficiaryService;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryIdentificationTuple;
import se.vgregion.service.glasogonbidrag.types.filter.BeneficiaryFilter;
import se.vgregion.service.glasogonbidrag.types.filter.BeneficiaryOrder;
import se.vgregion.service.glasogonbidrag.util.SharedStringMethod;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

        LOGGER.debug("Persisting identification: {}", identification);
        em.persist(identification);

        // Update creation date and modification date
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        beneficiary.setCreateDate(date);
        beneficiary.setModifiedDate(date);

        LOGGER.debug("Persisting beneficiary: {}", beneficiary);
        em.persist(beneficiary);
    }

    @Transactional(rollbackFor = { NoIdentificationException.class })
    public void create(Beneficiary beneficiary, Prescription prescription,
                       long userId,
                       long groupId,
                       long companyId,
                       Date date)
            throws NoIdentificationException {
        this.create(beneficiary);

        // Set user, group and company id of new prescription.
        prescription.setUserId(userId);
        prescription.setGroupId(groupId);
        prescription.setCompanyId(companyId);

        // Update creation date and modification date of new prescription.
        prescription.setCreateDate(date);
        prescription.setModifiedDate(date);

        LOGGER.debug(
                "Map prescription to beneficiary. Persisting prescription: {}",
                prescription);
        // Map prescription to beneficiary and store it
        beneficiary.getPrescriptionHistory().add(prescription);
        prescription.setBeneficiary(beneficiary);

        em.persist(prescription);
    }

    @Transactional(rollbackFor = { NoIdentificationException.class })
    public BeneficiaryIdentificationTuple update(Beneficiary beneficiary)
            throws NoIdentificationException {
        // Require that identification is set.
        Identification identification = beneficiary.getIdentification();

        if (identification == null) {
            throw new NoIdentificationException("Identification is not set.");
        }

        LOGGER.debug("Updating identification: {}", identification);
        Identification newIdentification = em.merge(identification);

        // Update modification date
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        beneficiary.setModifiedDate(date);

        LOGGER.debug("Updating beneficiary: {}", beneficiary);
        Beneficiary newBeneficiary = em.merge(beneficiary);

        return new BeneficiaryIdentificationTuple(
                newBeneficiary, newIdentification);
    }

    @Transactional
    public void delete(Long id) {
        Beneficiary beneficiary = em.find(Beneficiary.class, id);

        Identification identification = beneficiary.getIdentification();

        LOGGER.debug("Deleting beneficiary: {}", beneficiary);
        em.remove(beneficiary);

        LOGGER.debug("Deleting identification: {}", identification);
        em.remove(identification);
    }

    @Transactional
    public void delete(Long id, boolean removeAll) {
        Beneficiary beneficiary = findWithParts(id);

        if (beneficiary.getGrants().size() > 0) {
            throw new IllegalArgumentException(
                    "May not remove beneficiary with assigned grants.");
        }

        Set<Prescription> prescriptions =
                beneficiary.getPrescriptionHistory();

        for (Prescription prescription : prescriptions) {
            LOGGER.debug("Deleting prescription: {}", prescription);
            em.remove(prescription);
        }

        Identification identification = beneficiary.getIdentification();

        LOGGER.debug("Deleting beneficiary: {}", beneficiary);
        em.remove(beneficiary);

        LOGGER.debug("Deleting identification: {}", identification);
        em.remove(identification);
    }

    @Override
    public Beneficiary find(Long id) {
        return em.find(Beneficiary.class, id);
    }

    @Override
    @Transactional
    public Beneficiary findWithParts(Long id) {
        TypedQuery<Beneficiary> q = em.createNamedQuery(
                "glasogonbidrag.beneficiary.findWithParts", Beneficiary.class);
        q.setParameter("id", id);

        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Beneficiary findWithPartsByIdent(Identification identification) {
        TypedQuery<Beneficiary> q = em.createNamedQuery(
                "glasogonbidrag.beneficiary.findWithPartsByIdent",
                Beneficiary.class);
        q.setParameter("id", identification);

        try {
            return q.getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Beneficiary> findAll() {
        TypedQuery<Beneficiary> q = em.createNamedQuery(
                "glasogonbidrag.beneficiary.findAll", Beneficiary.class);

        return q.getResultList();
    }

    @Override
    @Transactional
    public List<Beneficiary> findAllWithParts() {
        TypedQuery<Beneficiary> q = em.createNamedQuery(
                "glasogonbidrag.beneficiary.findAllWithParts", Beneficiary.class);

        return q.getResultList();
    }

    @Override
    public List<BeneficiaryDTO> findAllFiltered(int firstResults,
                                                int maxResult,
                                                BeneficiaryFilter filters,
                                                BeneficiaryOrder order) {

        String query =
                "SELECT new se.vgregion.portal.glasogonbidrag.domain.dto." +
                           "BeneficiaryDTO( " +
                                "b.id, i.number, b.fullName, COUNT(g) ) " +
                "FROM Beneficiary b " +
                "LEFT JOIN b.grants g " +
                "LEFT JOIN b.identification i ";

        if (filters.hasFilters()) {
            query += buildWhereCondition(filters);
        }

        query += "GROUP BY b.id, i.number ";

        if (order.hasOrderBy()) {
            List<String> orderBy = new ArrayList<>();

            if (order.isOrderByNumber()) {
                orderBy.add("i.number");
            }

            if (order.isOrderByFullName()) {
                orderBy.add("b.fullName");
            }

            if (order.isOrderByCount()) {
                orderBy.add("COUNT(g)");
            }

            query += "ORDER BY ";
            query += SharedStringMethod.join(orderBy, ", ").concat(" ");
            query += order.getOrderType().toString();
        } else {
            query += "ORDER BY b.id";
        }

        TypedQuery<BeneficiaryDTO> q =
                em.createQuery(query, BeneficiaryDTO.class);

        q.setFirstResult(firstResults);
        q.setMaxResults(maxResult);

        if (filters.hasFilters()) {
            setQueryParameters(q, filters);
        }

        List<BeneficiaryDTO> result = q.getResultList();

        LOGGER.debug("listBeneficiaries() - The query {} found {} results",
                query, result.size());

        return result;
    }

    @Override
    public int countFiltered(BeneficiaryFilter filters) {
        String query =
                "SELECT COUNT(*) " +
                "FROM Beneficiary b " +
                "LEFT JOIN b.identification i ";

        if (filters.hasFilters()) {
            query += buildWhereCondition(filters);
        }

//        query += "GROUP BY b.id, i.number ";

        TypedQuery<Long> q = em.createQuery(query, Long.class);

        if (filters.hasFilters()) {
            setQueryParameters(q, filters);
        }

        Long result;
        try {
            result = q.getSingleResult();
        } catch (NoResultException e) {
            result = 0L;
        }

        LOGGER.debug("countBeneficiaries() - " +
                        "The query {} got the result {}",
                query, result);

        return result.intValue();
    }

    private String buildWhereCondition(BeneficiaryFilter filters) {
        List<String> whereCases = new ArrayList<>();

        if (filters.hasNumberFilter()) {
            whereCases.add("UPPER(i.number) LIKE " +
                    "CONCAT('%'," +
                    "CONCAT(UPPER(:numberFilter), '%'))");
        }

        if (filters.hasFullNameFilter()) {
            whereCases.add("UPPER(b.fullName) LIKE " +
                    "CONCAT('%'," +
                    "CONCAT(UPPER(:fullNameFilter), '%'))");
        }

        return "WHERE ".concat(
                SharedStringMethod.join(whereCases, " AND ")).concat(" ");
    }

    private <T> void setQueryParameters(TypedQuery<T> q,
                                        BeneficiaryFilter filters) {
        if (filters.hasNumberFilter()) {
            q.setParameter(
                    "numberFilter",
                    filters.getNumber());
        }

        if (filters.hasFullNameFilter()) {
            q.setParameter(
                    "fullNameFilter",
                    filters.getFullName());
        }
    }
}
