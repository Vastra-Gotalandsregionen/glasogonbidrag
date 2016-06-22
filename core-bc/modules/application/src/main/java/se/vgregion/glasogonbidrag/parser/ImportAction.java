package se.vgregion.glasogonbidrag.parser;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class ImportAction {
    private String file;
    private String sheet;
    private int line;
    private ImportActionEvent action;
    private String[] row;
    private String extraData;

    public ImportAction(String file,
                        String sheet,
                        int line,
                        ImportActionEvent action,
                        String[] row) {
        this(file, sheet, line, action, row, null);
    }

    public ImportAction(String file,
                        String sheet,
                        int line,
                        ImportActionEvent action,
                        String[] row,
                        String extraData) {
        this.file = file;
        this.sheet = sheet;
        this.line = line;

        this.action = action;
        this.row = row;

        this.extraData = extraData;
    }

    public String getFile() {
        return file;
    }

    public String getSheet() {
        return sheet;
    }

    int getLine() {
        return line;
    }

    public ImportActionEvent getAction() {
        return action;
    }

    public String[] getRow() {
        return row;
    }

    boolean is(ImportActionEvent action) {
        return action == this.action;
    }

    public String getExtraData() {
        return extraData;
    }

    @Override
    public String toString() {
        return this.action.toString() + "(" + line + ")";
    }
}
