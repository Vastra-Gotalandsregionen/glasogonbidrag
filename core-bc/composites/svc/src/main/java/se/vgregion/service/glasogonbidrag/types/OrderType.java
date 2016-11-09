package se.vgregion.service.glasogonbidrag.types;


/**
 * @author Martin Lind - Monator Technologies AB
 */
public enum OrderType {
    ASC,
    DESC;

    public static OrderType parse(String order) {
        switch (order) {
            case "DESCENDING":
                return DESC;
            case "ASCENDING":
                return ASC;
            default:
                return valueOf(order);
        }
    }
}
