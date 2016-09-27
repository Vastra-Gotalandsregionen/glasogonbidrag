package se.vgregion.service.glasogonbidrag.integration.exception;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class IdentityFormatException extends RuntimeException {
    public IdentityFormatException() {
    }

    public IdentityFormatException(String message) {
        super(message);
    }

    public IdentityFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdentityFormatException(Throwable cause) {
        super(cause);
    }

    public IdentityFormatException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
