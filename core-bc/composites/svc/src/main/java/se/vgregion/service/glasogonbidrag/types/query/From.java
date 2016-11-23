package se.vgregion.service.glasogonbidrag.types.query;

/**
 * @author Martin Lind
 */
public class From implements JpqlTerm {
    private final String table;
    private final String variable;

    public From(String table, String variable) {
        this.table = table;
        this.variable = variable;
    }

    @Override
    public String toJpqlString() {
        return table + " " + variable;
    }
}
