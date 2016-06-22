package se.vgregion.glasogonbidrag.parser;

import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.model.ImportGrant;

import java.util.List;
import java.util.Stack;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class ParseOutputData {
    private ImportDocument current;
    private Stack<ImportDocument> documents;
    private Stack<ImportError> errors;

    ParseOutputData() {
        this.documents = new Stack<>();
        this.errors = new Stack<>();
    }

    public Stack<ImportDocument> getDocuments() {
        return documents;
    }

    public Stack<ImportError> getErrors() {
        return errors;
    }

    void addDocument(ImportDocument document) {
        this.current = document;
        documents.push(document);
    }

    void addError(ImportError error) {
        errors.push(error);
    }

    void addGrant(ImportGrant grant) {
        current.addGrant(grant);
    }

    public int sumGrants() {
        int sum = 0;
        for (ImportDocument document : documents) {
            sum += document.getGrants().size();
        }
        return sum;
    }
}
