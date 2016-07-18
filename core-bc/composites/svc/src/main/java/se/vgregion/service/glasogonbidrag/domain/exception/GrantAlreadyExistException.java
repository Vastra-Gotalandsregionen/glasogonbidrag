package se.vgregion.service.glasogonbidrag.domain.exception;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class GrantAlreadyExistException extends Exception {
    public GrantAlreadyExistException() {
    }

    public GrantAlreadyExistException(String message) {
        super(message);
    }

    public GrantAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrantAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public GrantAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
