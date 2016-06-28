package se.vgregion.glasogonbidrag.parser;

import se.vgregion.glasogonbidrag.model.IdentificationType;
import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.model.ImportErrorType;
import se.vgregion.glasogonbidrag.model.ImportGrant;
import se.vgregion.glasogonbidrag.model.ValidationError;
import se.vgregion.glasogonbidrag.util.DocumentUtil;
import se.vgregion.glasogonbidrag.util.IdentificationUtil;
import se.vgregion.glasogonbidrag.util.ImportValidationUtil;

import java.util.Random;

/**
 * @author Martin Lind - Monator Technologies AB
 */
class ImportHandler {

    private static final String DUMMY_VERIFICATION_NUMBER_PREFIX = "DM";
    private static final String DUMMY_VERIFICATION_NUMBER;

    static {
        DUMMY_VERIFICATION_NUMBER = generateRandomVerificationNumber();
    }

    private static String generateRandomVerificationNumber() {
        Random rand = new Random();
        int a = rand.nextInt();

        return String.format("%s%d",
                DUMMY_VERIFICATION_NUMBER_PREFIX,
                Math.abs(a) % 100000000);
    }

    public static String dummyVerificationNumber() {
        return DUMMY_VERIFICATION_NUMBER;
    }

    private ParseOutputData data;

    ImportHandler(ParseOutputData data) {
        this.data = data;
    }

    /**
     * Validates the ID field in the action.
     * The id is located at the first column in the data row.
     *
     * @param action The import action to validate.
     * @return True if this should be stored. False if it should be skipped.
     */
    boolean validateId(ImportAction action) {
        String id = action.getRow()[0];

        IdentificationType type = IdentificationUtil.detect(id);
        if (type == IdentificationType.INVALID) {
            validation(action, true,
                    "the identification number is not valid");
        }

        return true;
    }

    /**
     * Validates the Grant row in the action.
     * The grant have data located at the second to sixth column in
     * the data row.
     *
     * @param action The import action to validate.
     * @return True if this should be stored. False if it should be skipped.
     */
    boolean validateGrant(ImportAction action) {
        String[] row = action.getRow();
        if (row.length < 6) {
            validation(action, false, "grant row contains to few columns.");
            return false;
        }

        boolean shouldStore = true;

        String deliveryDate = row[1];
        String prescriptionDate = row[2];
        String amount = row[3];
        String invoiceNumber = row[4];
        String verificationNumber = row[5];

        if (deliveryDate.trim().isEmpty()) {
            validation(action, false, "delivery date is empty.");
            shouldStore = false;
        }

        if (prescriptionDate.trim().isEmpty()) {
            validation(action, false, "prescription date is empty.");
            shouldStore = false;
        }

        if (amount.trim().isEmpty()) {
            validation(action, false, "amount is empty.");
            shouldStore = false;
        }

        if (invoiceNumber.trim().isEmpty()) {
            validation(action, true, "invoice number is empty.");
        }

        if (verificationNumber.trim().isEmpty()) {
            validation(action, false, "verification number is empty.");
            shouldStore = false;
        }

        return shouldStore;
    }

    void document(String id, ImportAction action) {
        ImportDocument doc = DocumentUtil.document(id, action.getRow());
        data.addDocument(doc);
    }

    void grant(ImportAction action) {
        ImportGrant grant = DocumentUtil.grant(action.getRow());

        if(!ImportValidationUtil.verificationNumber(action)) {
            String verificationNumber =
                    ImportHandler.dummyVerificationNumber();

            if (ImportValidationUtil.comment(action) &&
                    ImportValidationUtil
                            .verificationInComment(action)) {
                verificationNumber = DocumentUtil
                        .extractVerification(action.getExtraData());
            }

            DocumentUtil.replace(grant, verificationNumber);
        }

        data.addGrant(grant);
    }

    void error(ImportAction action, ImportErrorType type, String message) {
        ImportError error = new ImportError(
                action.getFile(),
                action.getSheet(),
                action.getLine(),
                type,
                message);

        data.addImportError(error);
    }

    private void validation(ImportAction action,
                            boolean shouldStore, String message) {
        ValidationError error = new ValidationError(
                action.getFile(),
                action.getSheet(),
                action.getLine(),
                shouldStore,
                message);

        data.addValidationError(error);
    }
}
