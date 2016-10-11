package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface SupplierService {
    void create(Supplier supplier);

    void update(Supplier supplier);

    void delete(Long id);

    Supplier find(long id);
    Supplier findWithInvoices(long id);

    List<Supplier> findAll();
    List<Supplier> findAllWithInvoices();
    List<Supplier> findAllActive();
    List<Supplier> findAllInactive();
    List<Supplier> findAllByName(String name);

}
