package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.dto.BeneficiaryDTO;
import se.vgregion.service.glasogonbidrag.types.LowLevelSortOrder;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface LowLevelDatabaseQueryService {

    int countBeneficiaries();

    List<BeneficiaryDTO> listBeneficiaries(
            LowLevelSortOrder sort, int firstResults, int maxResult)
                throws Exception;
}
