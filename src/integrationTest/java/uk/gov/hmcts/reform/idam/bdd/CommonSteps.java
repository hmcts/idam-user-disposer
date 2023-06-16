package uk.gov.hmcts.reform.idam.bdd;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;
import uk.gov.hmcts.reform.idam.helper.RestHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CommonSteps {

    @LocalServerPort
    private int port;

    //private int httpStatusResponseCode;

    final RestHelper restHelper = new RestHelper();

    private String getUrl(final String path) {
        return "http://localhost:" + port + path;
    }

    @Given("^Idam application is healthy$")
    public void checkApplication() {
        final Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .baseUri(getUrl(""))
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .when()
            .get()
            .andReturn();

        assertThat(response.getBody().asString()).contains("Welcome");
    }

}
