package se.vgregion.glasogonbidrag.datamodel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierDTO;
import se.vgregion.service.glasogonbidrag.domain.api.service.LowLevelDatabaseQueryService;
import se.vgregion.service.glasogonbidrag.domain.api.service.SupplierService;
import se.vgregion.service.glasogonbidrag.types.LowLevelSortOrder;
import se.vgregion.service.glasogonbidrag.types.OrderType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Lind - Monator Tehcnologies AB
 */
public class SupplierLazyDataModel extends LazyDataModel<SupplierDTO> {
    private final static Logger LOGGER =
            LoggerFactory.getLogger(SupplierLazyDataModel.class);

    private final static Map<String, String> VALUE_MAP = new HashMap<>();
    static {
        VALUE_MAP.put("name", "s.name");
        VALUE_MAP.put("externalServiceId", "s.externalServiceId");
        VALUE_MAP.put("count", "COUNT(i)");
        VALUE_MAP.put("active", "s.active");
    }

    private final SupplierService service;
    private final LowLevelDatabaseQueryService queryService;

    public SupplierLazyDataModel(
            SupplierService service,
            LowLevelDatabaseQueryService queryService) {
        this.service = service;
        this.queryService = queryService;
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
        List<SupplierDTO> results;

        try {
            LowLevelSortOrder sort = new LowLevelSortOrder(
                    sortField, "s.id",
                    OrderType.parse(sortOrder.toString()),
                    VALUE_MAP);
            results = queryService.listSuppliers(sort, first, pageSize);
        } catch (Exception e) {
            LOGGER.warn("Threw exception! {}", e.getMessage());

            results = new ArrayList<>();
        }

        this.setRowCount(queryService.countSuppliers() );

        return results;
    }
}
