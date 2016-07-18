package se.vgregion.service.glasogonbidrag.domain.api.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface SupplierRepository {
    Supplier find(long id);
    Supplier findWithInvoices(long id);

    List<Supplier> findAll();
    List<Supplier> findAllWithInvoices();
    List<Supplier> findAllActive();
    List<Supplier> findAllInactive();
    List<Supplier> findAllByName(String name);
}
