package se.vgregion.glasogonbidrag.parser;

import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.model.ImportGrant;
import se.vgregion.glasogonbidrag.model.ValidationError;

import java.util.Stack;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class ParseOutputData {
    private ImportDocument current;

    private Stack<ImportDocument> documents;
    private Stack<ImportError> importErrors;
    private Stack<ValidationError> validationErrors;

    ParseOutputData() {
        this.documents = new Stack<>();
        this.importErrors = new Stack<>();
        this.validationErrors = new Stack<>();
    }

    public Stack<ImportDocument> getDocuments() {
        return documents;
    }

    public Stack<ImportError> getImportErrors() {
        return importErrors;
    }

    public Stack<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    void addDocument(ImportDocument document) {
        this.current = document;
        documents.push(document);
    }

    void addGrant(ImportGrant grant) {
        current.addGrant(grant);
    }

    void addImportError(ImportError error) {
        importErrors.push(error);
    }

    void addValidationError(ValidationError error) {
        validationErrors.add(error);
    }
}
