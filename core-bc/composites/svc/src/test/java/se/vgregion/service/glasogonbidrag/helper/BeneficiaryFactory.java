package se.vgregion.service.glasogonbidrag.helper;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryFactory {
    /**
     * Hidden constructor, this class should not be instantiated.
     */
    private BeneficiaryFactory() {}

    @SuppressWarnings("WeakerAccess")
    public static BeneficiaryBuilder newBeneficiary(GrantBuilder parent) {
        return new BeneficiaryBuilder(parent);
    }

    public static BeneficiaryBuilder newBeneficiary() {
        return new BeneficiaryBuilder();
    }

    @SuppressWarnings("SameParameterValue")
    public static BeneficiaryBuilder newBeneficiary(String firstName,
                                                    String lastName) {
        return new BeneficiaryBuilder(firstName, lastName);
    }

    @SuppressWarnings("unused")
    public static BeneficiaryBuilder newBeneficiary(String fullName) {
        return new BeneficiaryBuilder(fullName);
    }
}
