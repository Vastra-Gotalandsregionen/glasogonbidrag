package se.vgregion.glasogonbidrag.model;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class ImportError {
    private String file;
    private String sheet;
    private int line;
    private ImportErrorType error;
    private String message;

    public ImportError(String file, String sheet, int line,
                       ImportErrorType error, String message) {
        this.file = file;
        this.sheet = sheet;
        this.line = line;
        this.error = error;
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

    public ImportErrorType getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
