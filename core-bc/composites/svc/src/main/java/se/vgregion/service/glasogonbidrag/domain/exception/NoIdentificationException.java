package se.vgregion.service.glasogonbidrag.domain.exception;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class NoIdentificationException extends Exception {
    public NoIdentificationException() {
    }

    public NoIdentificationException(String message) {
        super(message);
    }

    public NoIdentificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoIdentificationException(Throwable cause) {
        super(cause);
    }

    public NoIdentificationException(String message,
                                     Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
