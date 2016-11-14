package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.dto.BeneficiaryDTO;
import se.vgregion.portal.glasogonbidrag.domain.dto.InvoiceDTO;
import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierDTO;
import se.vgregion.service.glasogonbidrag.types.LowLevelSortOrder;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface LowLevelDatabaseQueryService {

    int countSuppliers();

    List<SupplierDTO> listSuppliers(
            LowLevelSortOrder sort, int firstResults, int maxResult)
                throws Exception;

    int countInvoices();

    List<InvoiceDTO> listInvoices(
            LowLevelSortOrder sort, int firstResults, int maxResult)
                throws Exception;

    int countBeneficiaries();

    List<BeneficiaryDTO> listBeneficiaries(
            LowLevelSortOrder sort, int firstResults, int maxResult)
                throws Exception;
}
