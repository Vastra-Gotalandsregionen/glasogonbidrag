package se.vgregion.glasogonbidrag.internal;

import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportGrant;

import java.util.Stack;

public class ParseOutputData {
    private ImportDocument current;
    private Stack<ImportDocument> documents;

    ParseOutputData() {
        this.documents = new Stack<>();
    }

    public void addDocument(ImportDocument document) {
        this.current = document;
        documents.push(document);
    }

    public void addGrant(ImportGrant grant) {
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
