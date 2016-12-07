package se.vgregion.glasogonbidrag.datamodel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.glasogonbidrag.domain.dto.InvoiceDTO;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.types.OrderType;
import se.vgregion.service.glasogonbidrag.types.filter.InvoiceFilter;
import se.vgregion.service.glasogonbidrag.types.filter.InvoiceOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Lind - Monator Tehcnologies AB
 */
public class InvoiceLazyDataModel extends LazyDataModel<InvoiceDTO> {
    private final static Logger LOGGER =
            LoggerFactory.getLogger(InvoiceLazyDataModel.class);

    private final InvoiceService service;

    public InvoiceLazyDataModel(InvoiceService service) {
        this.service = service;
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

        InvoiceFilter invoiceFilters = new InvoiceFilter(filters);
        InvoiceOrder invoiceOrder = new InvoiceOrder(
                sortField,
                OrderType.parse(sortOrder.toString()));

        List<InvoiceDTO> results;
        try {
            results = service.findAllFiltered(
                    first,
                    pageSize,
                    invoiceFilters,
                    invoiceOrder);
        } catch (Exception e) {
            LOGGER.warn("Threw exception! {}", e.getMessage());

            results = new ArrayList<>();
        }

        this.setRowCount(service.countFiltered(invoiceFilters));

        return results;
    }
}
