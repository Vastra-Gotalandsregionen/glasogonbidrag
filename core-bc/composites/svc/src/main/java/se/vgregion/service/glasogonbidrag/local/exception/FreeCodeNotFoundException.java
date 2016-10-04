package se.vgregion.service.glasogonbidrag.local.exception;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class FreeCodeNotFoundException extends RuntimeException {
    public FreeCodeNotFoundException() {
    }

    public FreeCodeNotFoundException(String message) {
        super(message);
    }

    public FreeCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FreeCodeNotFoundException(Throwable cause) {
        super(cause);
    }

    public FreeCodeNotFoundException(String message,
                                     Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
