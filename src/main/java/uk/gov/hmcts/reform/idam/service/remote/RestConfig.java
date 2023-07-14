package uk.gov.hmcts.reform.idam.service.remote;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest-client")
@Data
public class RestConfig {
    private String authorisationHeaderName;
    private String serviceAuthorisationHeaderName;
    private Long readTimeout = 60_000L;
    private Long connectTimeout = readTimeout;
}
