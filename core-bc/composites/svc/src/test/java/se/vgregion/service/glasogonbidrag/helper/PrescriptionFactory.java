package se.vgregion.service.glasogonbidrag.helper;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class PrescriptionFactory {
    /**
     * Hidden constructor, this class should not be instantiated.
     */
    private PrescriptionFactory() {}

    public static PrescriptionBuilder newPrescription() {
        return new PrescriptionBuilder();
    }

    @SuppressWarnings("unused")
    public static PrescriptionBuilder newPrescription(GrantBuilder parent) {
        return new PrescriptionBuilder(parent);
    }
}
