package se.vgregion.service.glasogonbidrag.domain.exception;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class GrantAdjustmentAlreadySetException extends Exception {
    public GrantAdjustmentAlreadySetException() {
    }

    public GrantAdjustmentAlreadySetException(String message) {
        super(message);
    }

    public GrantAdjustmentAlreadySetException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrantAdjustmentAlreadySetException(Throwable cause) {
        super(cause);
    }

    public GrantAdjustmentAlreadySetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
