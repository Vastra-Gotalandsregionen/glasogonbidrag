package se.vgregion.glasogonbidrag.datamodel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.glasogonbidrag.domain.dto.InvoiceDTO;
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
public class InvoiceLazyDataModel extends LazyDataModel<InvoiceDTO> {
    private final static Logger LOGGER =
            LoggerFactory.getLogger(InvoiceLazyDataModel.class);

    private final static Map<String, String> VALUE_MAP = new HashMap<>();
    static {
        VALUE_MAP.put("verificationNumber", "i.verificationNumber");
        VALUE_MAP.put("supplier", "s.name");
        VALUE_MAP.put("invoiceNumber", "i.invoiceNumber");
        VALUE_MAP.put("amount", "i.amount");
        VALUE_MAP.put("count", "COUNT(g)");
        VALUE_MAP.put("status", "i.status");
        VALUE_MAP.put("caseOwner", "i.caseOwner");
    }

    private final static Map<String, String> FILTERS_MAP = new HashMap<>();
    static {
        FILTERS_MAP.put("invoice-status-in-progress", "IN_PROGRESS");
        FILTERS_MAP.put("invoice-status-completed", "COMPLETED");
        FILTERS_MAP.put("invoice-status-canceled", "CANCELED");
        FILTERS_MAP.put("invoice-status-replaced", "REPLACED");
    }

    private final InvoiceService service;
    private final LowLevelDatabaseQueryService queryService;

    public InvoiceLazyDataModel(
            InvoiceService service,
            LowLevelDatabaseQueryService queryService) {
        this.service = service;
        this.queryService = queryService;
    }

    @Override
    public InvoiceDTO getRowData(String rowKey) {
        long id = Long.parseLong(rowKey);
        return new InvoiceDTO(service.findWithParts(id));
    }

    @Override
    public Object getRowKey(InvoiceDTO object) {
        return Long.toString(object.getId());
    }

    @Override
    public List<InvoiceDTO> load(int first,
                                 int pageSize,
                                 String sortField,
                                 SortOrder sortOrder,
                                 Map<String, Object> filters) {
        List<InvoiceDTO> results;

        try {
            LowLevelSortOrder sort = new LowLevelSortOrder(
                    sortField, "i.id",
                    OrderType.parse(sortOrder.toString()), filters,
                    VALUE_MAP, FILTERS_MAP);
            results = queryService.listInvoices(sort, first, pageSize);
        } catch (Exception e) {
            LOGGER.warn("Threw exception! {}", e.getMessage());

            results = new ArrayList<>();
        }

        this.setRowCount(queryService.countInvoices());

        return results;
    }
}
