package uk.gov.hmcts.reform.idam.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDef {
    private String today;
    private String actualAnswer;

    @Given("today is Sunday")
    public void todayIsSunday() {
        today = "Sunday";
    }

    @When("I ask whether it's Friday yet")
    public void askWeatherItsFridayYet() {
        actualAnswer = IsItFriday.itFridayToday(today);
    }

    @Then("I should be told {string}")
    public void shouldBeTold(String expectedAnswer) {
        assertEquals(expectedAnswer, actualAnswer,"not matching");
    }
}
