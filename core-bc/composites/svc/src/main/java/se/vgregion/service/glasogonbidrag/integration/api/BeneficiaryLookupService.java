package se.vgregion.service.glasogonbidrag.integration.api;

import se.vgregion.service.glasogonbidrag.types.BeneficiaryAreaTuple;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryNameTuple;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryTransport;

import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface BeneficiaryLookupService {
    BeneficiaryTransport fetchNameAndAddress(String identity, Date date);
    BeneficiaryNameTuple fetchName(String identity);

    /**
     *
     * @param identity
     * @param date
     * @return
     */
    BeneficiaryAreaTuple fetchAddress(String identity, Date date);
}
