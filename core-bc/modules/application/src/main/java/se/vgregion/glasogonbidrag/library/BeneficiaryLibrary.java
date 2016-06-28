package se.vgregion.glasogonbidrag.library;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
class BeneficiaryLibrary {
    private List<Beneficiary> beneficiaries;
    private List<Identification> identifications;

    public BeneficiaryLibrary() {
        beneficiaries = new ArrayList<>();
        identifications = new ArrayList<>();
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public List<Identification> getIdentifications() {
        return identifications;
    }

    public void addBeneficiary(Beneficiary beneficiary) {
        beneficiaries.add(beneficiary);
    }

    public void addIdentification(Identification id) {
        identifications.add(id);
    }

    public void add(Beneficiary beneficiary) {
        Identification identification = beneficiary.getIdentification();

        if (find(identification) == null) {
            identifications.add(identification);
            beneficiaries.add(beneficiary);
        } else {
            System.out.println("ERROR!");
        }
    }

    public void add(Beneficiary beneficiary, Identification identification) {
        beneficiary.setIdentification(identification);

        identifications.add(identification);
        beneficiaries.add(beneficiary);
    }

    public void assign(Identification identification, Grant grant) {
        Beneficiary beneficiary = find(identification);
        assign(beneficiary, grant);
    }

    public void assign(Beneficiary beneficiary, Grant grant) {
        beneficiary.getGrants().add(grant);
        grant.setBeneficiary(beneficiary);
    }

    public Beneficiary find(Identification identification) {
        for (Beneficiary beneficiary : beneficiaries) {
            if (beneficiary.getIdentification().equals(identification)) {
                return beneficiary;
            }
        }

        return null;
    }
}
