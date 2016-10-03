package se.vgregion.service.glasogonbidrag.integration.api;

import se.vgregion.service.glasogonbidrag.types.BeneficiaryAreaTransport;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryNameTransport;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryTransport;

import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface BeneficiaryLookupService {
    BeneficiaryTransport fetchNameAndAddress(String identity, Date date);
    BeneficiaryNameTransport fetchName(String identity);

    /**
     *
     * @param identity
     * @param date
     * @return
     */
    BeneficiaryAreaTransport fetchAddress(String identity, Date date);
}
