package se.vgregion.glasogonbidrag.datamodel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.service.glasogonbidrag.domain.api.service.BeneficiaryService;
import se.vgregion.portal.glasogonbidrag.domain.dto.BeneficiaryDTO;
import se.vgregion.service.glasogonbidrag.types.OrderType;
import se.vgregion.service.glasogonbidrag.types.lowlevel.BeneficiaryFilter;
import se.vgregion.service.glasogonbidrag.types.lowlevel.BeneficiaryOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Lind - Monator Tehcnologies AB
 */
public class BeneficiaryLazyDataModel extends LazyDataModel<BeneficiaryDTO> {
    private final static Logger LOGGER =
            LoggerFactory.getLogger(BeneficiaryLazyDataModel.class);

    private final BeneficiaryService service;

    public BeneficiaryLazyDataModel(BeneficiaryService service) {
        this.service = service;
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
        BeneficiaryFilter beneficiaryFilter = new BeneficiaryFilter(
                filters);
        BeneficiaryOrder beneficiaryOrder = new BeneficiaryOrder(
                sortField,
                OrderType.parse(sortOrder.toString()));

        List<BeneficiaryDTO> results;
        try {
            results = service.findAllFiltered(
                    first, pageSize, beneficiaryFilter, beneficiaryOrder);
        } catch (Exception e) {
            LOGGER.warn("Threw exception! {}", e.getMessage());

            results = new ArrayList<>();
        }

        this.setRowCount(service.countFiltered(beneficiaryFilter));

        return results;
    }
}
