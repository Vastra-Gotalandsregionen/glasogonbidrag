package se.vgregion.glasogonbidrag.datamodel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.service.glasogonbidrag.domain.api.service.BeneficiaryService;
import se.vgregion.portal.glasogonbidrag.domain.dto.BeneficiaryDTO;
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
public class BeneficiaryLazyDataModel extends LazyDataModel<BeneficiaryDTO> {
    private final static Logger LOGGER =
            LoggerFactory.getLogger(BeneficiaryLazyDataModel.class);


    private final static Map<String, String> VALUE_MAP = new HashMap<>();
    static {
        VALUE_MAP.put("number", "i.number");
        VALUE_MAP.put("fullName", "b.lastName");
        VALUE_MAP.put("count", "COUNT(g)");
    }

    private final static Map<String, String> FILTER_MAP = new HashMap<>();

    private final BeneficiaryService service;
    private final LowLevelDatabaseQueryService lowLevelService;

    public BeneficiaryLazyDataModel(
            BeneficiaryService service,
            LowLevelDatabaseQueryService lowLevelService) {
        this.service = service;
        this.lowLevelService = lowLevelService;
    }

    @Override
    public BeneficiaryDTO getRowData(String rowKey) {
        long id = Long.parseLong(rowKey);
        return new BeneficiaryDTO(service.findWithParts(id));
    }

    @Override
    public Object getRowKey(BeneficiaryDTO object) {
        return Long.toString(object.getId());
    }

    @Override
    public List<BeneficiaryDTO> load(int first,
                                     int pageSize,
                                     String sortField,
                                     SortOrder sortOrder,
                                     Map<String, Object> filters) {
        List<BeneficiaryDTO> results;

        try {
            LowLevelSortOrder sort = new LowLevelSortOrder(
                    sortField, "b.id",
                    OrderType.parse(sortOrder.toString()), filters,
                    VALUE_MAP, FILTER_MAP);
            results = lowLevelService.listBeneficiaries(sort, first, pageSize);
        } catch (Exception e) {
            LOGGER.warn("Threw exception! {}", e.getMessage());

            results = new ArrayList<>();
        }

        this.setRowCount(lowLevelService.countBeneficiaries());

        return results;
    }
}
