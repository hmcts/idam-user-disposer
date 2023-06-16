package uk.gov.hmcts.reform.idam.helper;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RestHelper {

    public Response getResponseWithoutHeader(final String path) {
        return RestAssured
            .given()
            .relaxedHTTPSValidation()
            .baseUri(path)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .when()
            .get()
            .andReturn();
    }
}
