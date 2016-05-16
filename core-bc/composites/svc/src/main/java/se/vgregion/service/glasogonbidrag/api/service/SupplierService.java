package se.vgregion.service.glasogonbidrag.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface SupplierService {
    void create(Supplier supplier);

    void update(Supplier supplier);

    void delete(Long id);
}
