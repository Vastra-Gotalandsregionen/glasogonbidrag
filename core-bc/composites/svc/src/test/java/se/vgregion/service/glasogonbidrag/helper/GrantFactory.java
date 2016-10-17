package se.vgregion.service.glasogonbidrag.helper;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class GrantFactory {
    public static GrantBuilder newGrant() {
        return new GrantBuilder();
    }
}
