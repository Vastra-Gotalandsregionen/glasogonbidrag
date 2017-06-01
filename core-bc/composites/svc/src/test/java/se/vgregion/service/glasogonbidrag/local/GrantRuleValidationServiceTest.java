package se.vgregion.service.glasogonbidrag.local;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GrantRuleValidationServiceTestShared.class,
        GrantRuleValidationServiceTestPost20160620.class,
        GrantRuleValidationServiceTestPre20160620.class
})
public class GrantRuleValidationServiceTest {}
