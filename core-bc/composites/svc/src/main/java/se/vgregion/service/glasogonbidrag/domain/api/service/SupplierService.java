package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.types.lowlevel.SupplierFilter;
import se.vgregion.service.glasogonbidrag.types.lowlevel.SupplierOrder;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface SupplierService {
    void create(Supplier supplier);

    Supplier update(Supplier supplier);

    void delete(Long id);

    Supplier find(long id);
    Supplier findWithInvoices(long id);

    List<Supplier> findAll();
    List<Supplier> findAllWithInvoices();
    List<Supplier> findAllActive();
    List<Supplier> findAllInactive();
    List<Supplier> findAllByName(String name);

    // Methods with filters

    List<SupplierDTO> findAllFiltered(int firstResults,
                                      int maxResult,
                                      SupplierFilter filters,
                                      SupplierOrder order);
    int countFiltered(SupplierFilter filters);
}
