package se.vgregion.service.glasogonbidrag.exception;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryHaveGrantsException extends Exception {
    public BeneficiaryHaveGrantsException() {
        super();
    }

    public BeneficiaryHaveGrantsException(String message) {
        super(message);
    }

    public BeneficiaryHaveGrantsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeneficiaryHaveGrantsException(Throwable cause) {
        super(cause);
    }

    protected BeneficiaryHaveGrantsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
