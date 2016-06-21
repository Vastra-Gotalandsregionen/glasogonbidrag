package se.vgregion.glasogonbidrag.parser;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class IllegalImportStateException extends RuntimeException {
    private int line;
    public IllegalImportStateException(int line) {
        this.line = line;
    }

    IllegalImportStateException(int line, String message) {
        super(message);
        this.line = line;
    }

    public IllegalImportStateException(int line,
                                       String message,
                                       Throwable cause) {
        super(message, cause);
        this.line = line;
    }

    public IllegalImportStateException(int line,
                                       Throwable cause) {
        super(cause);
        this.line = line;
    }

    public IllegalImportStateException(int line,
                                       String message,
                                       Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
