package se.vgregion.glasogonbidrag.datamodel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierInvoiceDTO;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.domain.api.service.LowLevelDatabaseQueryService;
import se.vgregion.service.glasogonbidrag.types.OrderType;
import se.vgregion.service.glasogonbidrag.types.lowlevel.InvoiceFilter;
import se.vgregion.service.glasogonbidrag.types.lowlevel.InvoiceOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Lind - Monator Tehcnologies AB
 */
public class SupplierInvoiceLazyDataModel
        extends LazyDataModel<SupplierInvoiceDTO> {
    private final static Logger LOGGER =
            LoggerFactory.getLogger(SupplierInvoiceLazyDataModel.class);

    private final long supplierId;
    private final InvoiceService service;

    public SupplierInvoiceLazyDataModel(long supplierId,
                                        InvoiceService service) {
        this.service = service;
        this.supplierId = supplierId;
    }

    @Override
    public SupplierInvoiceDTO getRowData(String rowKey) {
        long id = Long.parseLong(rowKey);
        return new SupplierInvoiceDTO(service.findWithParts(id));
    }

    @Override
    public Object getRowKey(SupplierInvoiceDTO object) {
        return Long.toString(object.getId());
    }

    @Override
    public List<SupplierInvoiceDTO> load(int first,
                                         int pageSize,
                                         String sortField,
                                         SortOrder sortOrder,
                                         Map<String, Object> filters) {
        InvoiceFilter invoiceFilter = new InvoiceFilter(filters);
        InvoiceOrder invoiceOrder = new InvoiceOrder(
                sortField,
                OrderType.parse(sortOrder.toString()));

        List<SupplierInvoiceDTO> results;
        try {
            results = service.findAllBySupplierFiltered(
                    supplierId, first, pageSize, invoiceFilter, invoiceOrder);
        } catch (Exception e) {
            LOGGER.warn("Threw exception! {}", e.getMessage());

            results = new ArrayList<>();
        }

        this.setRowCount(
                service.countBySupplierFiltered(supplierId, invoiceFilter));

        return results;
    }
}
