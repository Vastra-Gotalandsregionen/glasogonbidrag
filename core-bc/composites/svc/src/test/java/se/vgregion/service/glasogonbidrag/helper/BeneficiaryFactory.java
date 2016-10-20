package se.vgregion.service.glasogonbidrag.helper;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryFactory {
    /**
     * Hidden constructor, this class should not be instantiated.
     */
    private BeneficiaryFactory() {}

    public static BeneficiaryBuilder newBeneficiary(GrantBuilder parent) {
        return new BeneficiaryBuilder(parent);
    }

    public static BeneficiaryBuilder newBeneficiary() {
        return new BeneficiaryBuilder();
    }

    public static BeneficiaryBuilder newBeneficiary(String firstName,
                                                    String lastName) {
        return new BeneficiaryBuilder(firstName, lastName);
    }
}
