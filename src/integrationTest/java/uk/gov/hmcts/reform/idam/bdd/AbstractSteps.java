package uk.gov.hmcts.reform.idam.bdd;

import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static feign.form.ContentProcessor.CONTENT_TYPE_HEADER;
import static uk.gov.hmcts.reform.idam.bdd.WiremokInstantiator.INSTANCE;

@Getter
public class AbstractSteps {
    public final WiremokInstantiator wiremokInstantiator = INSTANCE;

    private static final String JSON_RESPONSE = "application/json;charset=UTF-8";

    protected final Gson jsonReader = new Gson();


    @LocalServerPort
    private int port;

    public String baseUrl() {
        return "http://localhost:" + port;
    }




    public void setupAuthorisationStub() {
        wiremokInstantiator.getWireMockServer().stubFor(get(urlPathMatching("/details"))
                .willReturn(aResponse()
                        .withHeader(
                                CONTENT_TYPE_HEADER,
                                JSON_RESPONSE
                        )
                        .withStatus(200)
                        .withBody("test")));
    }
}

