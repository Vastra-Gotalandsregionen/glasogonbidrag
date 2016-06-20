package se.vgregion.glasogonbidrag.model;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class ImportError {
    private String file;
    private String sheet;
    private int line;
    private String message;

    public ImportError(String file, String sheet, int line, String message) {
        this.file = file;
        this.sheet = sheet;
        this.line = line;
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

    public String getMessage() {
        return message;
    }
}
