package se.vgregion.service.glasogonbidrag.types;

import se.vgregion.service.glasogonbidrag.local.internal.GrantRuleValidationServiceImpl;

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

    public void addAllViolations(List<GrantRuleViolation> violations) {
        for (GrantRuleViolation violation : violations) {
            add(violation);
        }
    }

    public void addAllWarnings(List<GrantRuleWarning> warnings) {
        for (GrantRuleWarning warning : warnings) {
            add(warning);
        }
    }

    public List<GrantRuleViolation> getViolations() {
        return violations;
    }

    public List<GrantRuleWarning> getWarnings() {
        return warnings;
    }

    public List<String> getViolationStrings() {
        List<String> errorCodes = new ArrayList<>();

        for (GrantRuleViolation entry : violations) {
            errorCodes.add(entry.getErrorCode());
        }

        return errorCodes;
    }

    public List<String> getWarningStrings() {
        List<String> errorCodes = new ArrayList<>();

        for (GrantRuleWarning entry : warnings) {
            errorCodes.add(entry.getErrorCode());
        }

        return errorCodes;
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
