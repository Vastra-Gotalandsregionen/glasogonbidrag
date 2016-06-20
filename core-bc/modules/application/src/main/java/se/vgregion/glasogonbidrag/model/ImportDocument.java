package se.vgregion.glasogonbidrag.model;

import java.util.ArrayList;
import java.util.List;

public class ImportDocument {
    private String id;
    private String name;
    private List<ImportGrant> grants;

    public ImportDocument(String id, String name) {
        this.id = id;
        this.name = name;
        this.grants = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ImportGrant> getGrants() {
        return grants;
    }

    public void addGrant(ImportGrant grant) {
        grants.add(grant);
    }

    @Override
    public String toString() {
        return id + ":" + name + "(" + grants.size() + ")";
    }
}
