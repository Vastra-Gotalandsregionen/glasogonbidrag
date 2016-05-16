package se.vgregion.service.glasogonbidrag.api.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface SupplierRepository {
    Supplier find(String name);

    List<Supplier> findAll();
}
