package uk.gov.hmcts.reform.idam.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;

@SuppressWarnings({"PMD.JUnit4TestShouldUseBeforeAnnotation"})
public class IdamUserGetSteps extends AbstractSteps {

    @Before
    public void setUp() {
        setupAuthorisationStub();
    }

    @And("I am authorized")
    public void updateUserRoleAuditInvestigator() {
        setupAuthorisationStub();
    }

}
