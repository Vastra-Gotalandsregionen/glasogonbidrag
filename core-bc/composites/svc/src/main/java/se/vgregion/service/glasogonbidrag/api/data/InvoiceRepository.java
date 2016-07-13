package se.vgregion.service.glasogonbidrag.api.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface InvoiceRepository {
    Invoice find(Long id);

    Invoice findWithParts(Long id);

    Invoice findByVerificationNumber(String number);

    List<Invoice> findAll();

    List<Invoice> findAllOrderByModificationDate();

    List<Invoice> findAllOrderByModificationDate(long userId);

    List<Invoice> findAllBySupplier(Supplier supplier);

    List<Invoice> findAllByInvoiceNumber(String number);

    List<Invoice> findAllWithStatus(InvoiceStatus status);

    List<Invoice> findAllWithParts();
}
