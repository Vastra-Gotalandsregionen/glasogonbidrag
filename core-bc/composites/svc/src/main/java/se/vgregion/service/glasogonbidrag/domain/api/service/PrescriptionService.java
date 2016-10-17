package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface PrescriptionService {

    Prescription find(long id);

    Prescription findLatest(Beneficiary beneficiary);

}
