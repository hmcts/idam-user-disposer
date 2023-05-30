package uk.gov.hmcts.reform.idam.config;

import com.microsoft.applicationinsights.TelemetryClient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppInsightsConfigurationTest {
    @Test
    void shouldCreateAppInsightsClient() {
        final AppInsightsConfiguration appInsightsConfig = new AppInsightsConfiguration();
        assertThat(appInsightsConfig.telemetryClient()).isInstanceOf(TelemetryClient.class);
    }
}
