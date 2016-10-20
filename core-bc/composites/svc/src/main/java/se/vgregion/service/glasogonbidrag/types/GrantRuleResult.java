package se.vgregion.service.glasogonbidrag.types;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class GrantRuleResult {
    private List<GrantRuleViolation> violations = new ArrayList<>();
    private List<GrantRuleWarning> warnings = new ArrayList<>();

    public void add(GrantRuleViolation violation) {
        if (!violations.contains(violation)) {
            violations.add(violation);
        }
    }

    public void add(GrantRuleWarning warning) {
        if (!warnings.contains(warning)) {
            warnings.add(warning);
        }
    }

    public List<GrantRuleViolation> getViolations() {
        return violations;
    }

    public List<GrantRuleWarning> getWarnings() {
        return warnings;
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public boolean hasViolations() {
        return !violations.isEmpty();
    }

    public int warnings() {
        return warnings.size();
    }

    public int violations() {
        return violations.size();
    }
}
