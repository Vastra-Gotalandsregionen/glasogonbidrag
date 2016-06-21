package se.vgregion.glasogonbidrag.parser;

import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportGrant;

import java.util.Stack;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class ParseOutputData {
    private ImportDocument current;
    private Stack<ImportDocument> documents;

    ParseOutputData() {
        this.documents = new Stack<>();
    }

    void addDocument(ImportDocument document) {
        this.current = document;
        documents.push(document);
    }

    void addGrant(ImportGrant grant) {
        current.addGrant(grant);
    }

    public Stack<ImportDocument> getDocuments() {
        return documents;
    }

    public int sumGrants() {
        int sum = 0;
        for (ImportDocument document : documents) {
            sum += document.getGrants().size();
        }
        return sum;
    }
}
