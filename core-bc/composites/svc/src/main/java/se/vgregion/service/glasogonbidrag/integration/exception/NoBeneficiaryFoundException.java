package se.vgregion.service.glasogonbidrag.integration.exception;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class NoBeneficiaryFoundException extends RuntimeException {
    public NoBeneficiaryFoundException() {
    }

    public NoBeneficiaryFoundException(String message) {
        super(message);
    }

    public NoBeneficiaryFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoBeneficiaryFoundException(Throwable cause) {
        super(cause);
    }

    public NoBeneficiaryFoundException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
