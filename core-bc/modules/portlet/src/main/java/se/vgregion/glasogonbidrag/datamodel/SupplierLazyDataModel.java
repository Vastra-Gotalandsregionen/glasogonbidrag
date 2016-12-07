package se.vgregion.glasogonbidrag.datamodel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierDTO;
import se.vgregion.service.glasogonbidrag.domain.api.service.SupplierService;
import se.vgregion.service.glasogonbidrag.types.OrderType;
import se.vgregion.service.glasogonbidrag.types.filter.SupplierFilter;
import se.vgregion.service.glasogonbidrag.types.filter.SupplierOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Lind - Monator Tehcnologies AB
 */
public class SupplierLazyDataModel extends LazyDataModel<SupplierDTO> {
    private final static Logger LOGGER =
            LoggerFactory.getLogger(SupplierLazyDataModel.class);

    private final SupplierService service;

    public SupplierLazyDataModel(SupplierService service) {
        this.service = service;
    }

    @Override
    public SupplierDTO getRowData(String rowKey) {
        long id = Long.parseLong(rowKey);
        return new SupplierDTO(service.findWithInvoices(id));
    }

    @Override
    public Object getRowKey(SupplierDTO object) {
        return Long.toString(object.getId());
    }

    @Override
    public List<SupplierDTO> load(int first,
                                  int pageSize,
                                  String sortField,
                                  SortOrder sortOrder,
                                  Map<String, Object> filters) {
        SupplierFilter supplierFilter = new SupplierFilter(filters);
        SupplierOrder supplierOrder = new SupplierOrder(
                sortField,
                OrderType.parse(sortOrder.toString()));

        List<SupplierDTO> results;
        try {
            results = service.findAllFiltered(
                    first, pageSize, supplierFilter, supplierOrder);
        } catch (Exception e) {
            LOGGER.warn("Threw exception! {}", e.getMessage());

            results = new ArrayList<>();
        }

        this.setRowCount(service.countFiltered(supplierFilter));

        return results;
    }
}
