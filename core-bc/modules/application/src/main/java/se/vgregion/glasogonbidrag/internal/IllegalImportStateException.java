package se.vgregion.glasogonbidrag.internal;

public class IllegalImportStateException extends RuntimeException {
    public IllegalImportStateException(int line) {
    }

    public IllegalImportStateException(int line,
                                       String message) {
        super(message);
    }

    public IllegalImportStateException(int line,
                                       String message,
                                       Throwable cause) {
        super(message, cause);
    }

    public IllegalImportStateException(int line,
                                       Throwable cause) {
        super(cause);
    }

    public IllegalImportStateException(int line,
                                       String message,
                                       Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
