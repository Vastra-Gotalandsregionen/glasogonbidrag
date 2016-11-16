package se.vgregion.glasogonbidrag.datamodel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierInvoiceDTO;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.domain.api.service.LowLevelDatabaseQueryService;
import se.vgregion.service.glasogonbidrag.types.LowLevelSortOrder;
import se.vgregion.service.glasogonbidrag.types.OrderType;

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

    private final static Map<String, String> VALUE_MAP = new HashMap<>();
    static {
        VALUE_MAP.put("verificationNumber", "i.verificationNumber");
        VALUE_MAP.put("status", "i.status");
        VALUE_MAP.put("caseWorker", "i.caseWorker");
    }

    private final static Map<String, String> FILTER_MAP = new HashMap<>();
    static {
        FILTER_MAP.put("invoice-status-in-progress", "IN_PROGRESS");
        FILTER_MAP.put("invoice-status-completed", "COMPLETED");
        FILTER_MAP.put("invoice-status-canceled", "CANCELED");
        FILTER_MAP.put("invoice-status-replaced", "REPLACED");
    }

    private final long supplierId;
    private final InvoiceService invoiceService;
    private final LowLevelDatabaseQueryService queryService;

    public SupplierInvoiceLazyDataModel(
            long supplierId,
            InvoiceService invoiceService,
            LowLevelDatabaseQueryService queryService) {
        this.queryService = queryService;
        this.invoiceService = invoiceService;
        this.supplierId = supplierId;
    }

    @Override
    public SupplierInvoiceDTO getRowData(String rowKey) {
        long id = Long.parseLong(rowKey);
        return new SupplierInvoiceDTO(invoiceService.findWithParts(id));
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
        List<SupplierInvoiceDTO> results;


        try {
            LowLevelSortOrder sort = new LowLevelSortOrder(
                    sortField, "i.id",
                    OrderType.parse(sortOrder.toString()),
                    filters,
                    VALUE_MAP, FILTER_MAP);
            results = queryService.listInvoicesForSupplier(
                    supplierId, sort, first, pageSize);
        } catch (Exception e) {
            LOGGER.warn("Threw exception! {}", e.getMessage());

            results = new ArrayList<>();
        }

        this.setRowCount(queryService.countInvoicesForSupplier(supplierId));

        return results;
    }
}
