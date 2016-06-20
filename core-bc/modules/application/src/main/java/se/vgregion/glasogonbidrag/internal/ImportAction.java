package se.vgregion.glasogonbidrag.internal;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class ImportAction {
    private int line;
    private ImportActionEvent action;
    private String[] row;

    public ImportAction(int line,
                        ImportActionEvent action,
                        String[] row) {
        this.line = line;
        this.action = action;
        this.row = row;
    }

    int getLine() {
        return line;
    }

    public ImportActionEvent getAction() {
        return action;
    }

    String[] getRow() {
        return row;
    }

    boolean is(ImportActionEvent action) {
        return action == this.action;
    }

    @Override
    public String toString() {
        return this.action.toString() + "(" + line + ")";
    }
}
