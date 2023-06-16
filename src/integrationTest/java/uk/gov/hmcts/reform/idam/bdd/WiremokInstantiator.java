package uk.gov.hmcts.reform.idam.bdd;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;

@Getter
@SuppressWarnings({"PMD.NonSerializableClass"})
public enum WiremokInstantiator {
    INSTANCE;

    private final WireMockServer wireMockServer = new WireMockServer(4554);

    WiremokInstantiator() {
        wireMockServer.start();
    }
}
