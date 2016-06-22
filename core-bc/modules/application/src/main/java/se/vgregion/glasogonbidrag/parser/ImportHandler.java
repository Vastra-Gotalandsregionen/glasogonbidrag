package se.vgregion.glasogonbidrag.parser;

import se.vgregion.glasogonbidrag.model.IdentificationType;
import se.vgregion.glasogonbidrag.model.ImportDataLibrary;
import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.model.ImportErrorType;
import se.vgregion.glasogonbidrag.model.ImportGrant;
import se.vgregion.glasogonbidrag.model.ValidationError;
import se.vgregion.glasogonbidrag.util.DocumentUtil;
import se.vgregion.glasogonbidrag.util.IdentificationUtil;
import se.vgregion.glasogonbidrag.util.ImportValidationUtil;

/**
 * @author Martin Lind - Monator Technologies AB
 */
class ImportHandler {
    private ParseOutputData data;

    ImportHandler(ParseOutputData data) {
        this.data = data;
    }

    void validate(ImportAction action) {
        String id = action.getRow()[0];

        IdentificationType type = IdentificationUtil.detect(id);
        if (type == IdentificationType.INVALID) {
            validation(action, ""); //TODO: Write message
        }
    }

    void document(String id, ImportAction action) {
        ImportDocument doc = DocumentUtil.document(id, action.getRow());
        data.addDocument(doc);
    }

    void grant(ImportAction action) {
        ImportGrant grant = DocumentUtil.grant(action.getRow());

        if(!ImportValidationUtil.verificationNumber(action)) {
            String verificationNumber =
                    ImportDataLibrary.dummyVerificationNumber();

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

    void validation(ImportAction action, String message) {
        ValidationError error = new ValidationError();
        // TODO Write this data.

        data.addValidationError(error);
    }
}
