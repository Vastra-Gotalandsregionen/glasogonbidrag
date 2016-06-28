package se.vgregion.glasogonbidrag.library;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
class GrantLibrary {
    private List<Grant> grants;

    public GrantLibrary() {
        grants = new ArrayList<>();
    }

    public List<Grant> getGrants() {
        return grants;
    }

    public void add(Grant grant) {
        grants.add(grant);
    }
}
