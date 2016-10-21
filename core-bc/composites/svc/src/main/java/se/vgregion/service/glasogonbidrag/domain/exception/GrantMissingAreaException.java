package se.vgregion.service.glasogonbidrag.domain.exception;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class GrantMissingAreaException extends Exception {
    public GrantMissingAreaException() {
    }

    public GrantMissingAreaException(String message) {
        super(message);
    }

    public GrantMissingAreaException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrantMissingAreaException(Throwable cause) {
        super(cause);
    }

    public GrantMissingAreaException(String message,
                                     Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
