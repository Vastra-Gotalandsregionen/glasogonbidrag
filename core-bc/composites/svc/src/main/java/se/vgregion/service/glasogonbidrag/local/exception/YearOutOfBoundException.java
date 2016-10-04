package se.vgregion.service.glasogonbidrag.local.exception;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class YearOutOfBoundException extends RuntimeException {
    public YearOutOfBoundException() {
    }

    public YearOutOfBoundException(String message) {
        super(message);
    }

    public YearOutOfBoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public YearOutOfBoundException(Throwable cause) {
        super(cause);
    }

    public YearOutOfBoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
