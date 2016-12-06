package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.dto.InvoiceDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.*;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantAlreadyExistException;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantMissingAreaException;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;
import se.vgregion.service.glasogonbidrag.types.InvoiceBeneficiaryIdentificationTuple;
import se.vgregion.service.glasogonbidrag.types.InvoiceBeneficiaryTuple;
import se.vgregion.service.glasogonbidrag.types.InvoiceGrantTuple;
import se.vgregion.service.glasogonbidrag.types.lowlevel.InvoiceFilter;
import se.vgregion.service.glasogonbidrag.types.lowlevel.InvoiceOrder;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface InvoiceService {
    void create(long userId, long groupId, long companyId,
                String caseWorker,
                Invoice invoice);

    Invoice update(String caseWorker, Invoice invoice);

    InvoiceBeneficiaryIdentificationTuple updateAddGrant(
            long userId, long groupId, long companyId,
            String caseWorker,
            Invoice invoice, Grant grant)
        throws GrantAlreadyExistException,
               GrantMissingAreaException,
               NoIdentificationException;

    InvoiceGrantTuple updateGrant(String caseWorker,
                                  Invoice invoice,
                                  Grant grant)
        throws GrantMissingAreaException;

    InvoiceBeneficiaryTuple updateDeleteGrant(String caseWorker,
                                              Invoice invoice,
                                              long grantId);

    Invoice updateAddAccountingDistribution(
            long userId, long groupId, long companyId,
            String caseWorker,
            Invoice invoice, AccountingDistribution distribution);

    void delete(Long id);

    Invoice find(Long id);

    Invoice findWithParts(Long id);

    Invoice findByVerificationNumber(String number);

    List<Invoice> findAll();

    List<Invoice> findAll(int firstResult, int maxResults);

    List<Invoice> findAllWithParts();

    List<Invoice> findAllOrderByModificationDate();

    List<Invoice> findAllOrderByModificationDate(
            long userId, int firstResult, int maxResults);

    List<Invoice> findAllBySupplier(Supplier supplier);

    List<Invoice> findAllBySupplier(Supplier supplier,
                                    int firstResult,
                                    int maxResults);

    List<Invoice> findAllByInvoiceNumber(String number);

    List<Invoice> findAllWithStatus(InvoiceStatus status);

    List<Invoice> findAllWithStatus(InvoiceStatus status,
                                    long userId);

    List<Invoice> findAllWithStatus(InvoiceStatus status,
                                    long userId,
                                    int firstResult,
                                    int maxResults);

    List<Invoice> findAllByCaseWorker(String caseWorker);

    List<Invoice> findAllWithStatusOrderByModificationDate(
            InvoiceStatus status);

    List<Invoice> findAllWithStatusOrderByModificationDate(
            InvoiceStatus status, int firstResult, int maxResults);

    // Methods with filters

    List<InvoiceDTO> findAllFiltered(int firstResults,
                                     int maxResult,
                                     InvoiceFilter filters,
                                     InvoiceOrder order);

    int countFiltered(InvoiceFilter filters);
}
