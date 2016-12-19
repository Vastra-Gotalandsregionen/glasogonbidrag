package se.vgregion.portal.glasogonbidrag.domain.dto;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class ImportDTO {
    private final List<Beneficiary> beneficiaries;
    private final List<Prescription> prescriptions;

    public ImportDTO() {
        this(new ArrayList<Beneficiary>(), new ArrayList<Prescription>());
    }

    public ImportDTO(List<Beneficiary> beneficiaries,
                     List<Prescription> prescriptions) {
        this.beneficiaries = beneficiaries;
        this.prescriptions = prescriptions;
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }
}
