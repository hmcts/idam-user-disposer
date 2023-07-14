package uk.gov.hmcts.reform.idam;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.contract.wiremock.WireMockConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.idam.service.StaleUsersService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 4554, stubs = {"classpath:/mappings/*.json"})
class ApplicationIntegrationTest {

    @Autowired
    private StaleUsersService service;

    @Autowired
    WireMockServer wireMock;

    private static final String FLAG = "true";

    @Test
    void testShouldBootstrapSpringContext() {
        WireMock.listAllStubMappings();
        assertThat(Boolean.valueOf(FLAG)).isTrue();
    }

    @Test
    void testShouldReturnUsersWithoutRoles() {
        // probably worth re-factoring a bit, so it is clearer
        // what we pass in, and what we get back
        var response = service.filterUsersWithRoleAssignments(service.retrieveStaleUsers());
        assertThat(response)
            .contains("003", "004", "005", "006", "007", "008", "009")
            .doesNotContain("001", "002", "010");
    }


    @TestConfiguration
    static class WireMockTestConfiguration {
        @Bean
        WireMockConfigurationCustomizer optionsCustomizer() {
            return config -> config.notifier(new ConsoleNotifier(true));
        }
    }

}

