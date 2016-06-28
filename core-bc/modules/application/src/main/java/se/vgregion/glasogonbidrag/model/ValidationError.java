package se.vgregion.glasogonbidrag.model;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class ValidationError {
    private String file;
    private String sheet;
    private int line;
    private boolean shouldStore;
    private String message;

    public ValidationError(String file, String sheet, int line,
                           boolean shouldStore, String message) {
        this.file = file;
        this.sheet = sheet;
        this.line = line;
        this.shouldStore = shouldStore;
        this.message = message;
    }

    public String getFile() {
        return file;
    }

    public String getSheet() {
        return sheet;
    }

    public int getLine() {
        return line;
    }

    public boolean shouldStore() {
        return shouldStore;
    }

    public String getMessage() {
        return message;
    }
}
