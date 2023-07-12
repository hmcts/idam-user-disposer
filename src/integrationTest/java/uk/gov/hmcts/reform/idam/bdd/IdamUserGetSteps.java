package uk.gov.hmcts.reform.idam.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;

public class IdamUserGetSteps extends AbstractSteps {

    @SuppressWarnings("PMD.JUnit4TestShouldUseBeforeAnnotation")
    @Before
    public void setUp() {
        setupAuthorisationStub();
    }

    @And("I am authorized")
    public void updateUserRoleAuditInvestigator() {
        setupAuthorisationStub();
    }

}
