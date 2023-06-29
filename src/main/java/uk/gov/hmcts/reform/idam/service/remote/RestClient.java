package uk.gov.hmcts.reform.idam.service.remote;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.idam.util.SecurityUtil;

import java.util.Map;

import static org.glassfish.jersey.client.ClientProperties.CONNECT_TIMEOUT;
import static org.glassfish.jersey.client.ClientProperties.READ_TIMEOUT;

@Service
@Getter
public class RestClient {
    private final SecurityUtil securityUtil;
    private final RestConfig restConfig;
    private Client client;


    public RestClient(final SecurityUtil securityUtil, final RestConfig restConfig) {
        this.securityUtil = securityUtil;
        this.restConfig = restConfig;
        this.client = buildClient();
    }

    public Response getRequest(final String host, final String path, final Map<String, Object> params) {

        WebTarget target = client
            .target(host)
            .path(path);

        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue().toString());
            }
        }

        return target.request(MediaType.APPLICATION_JSON_TYPE)
            .header(restConfig.getAuthorisationHeaderName(), securityUtil.getServiceAuthorization())
            .header(restConfig.getServiceAuthorisationHeaderName(), securityUtil.getIdamClientToken())
            .get();
    }

    private Client buildClient() {
        final ClientConfig clientConfig = new ClientConfig()
            .property(READ_TIMEOUT, restConfig.getReadTimeout())
            .property(CONNECT_TIMEOUT, restConfig.getConnectTimeout());

        return ClientBuilder
            .newClient(clientConfig)
            .register(new LoggingFeature());
    }
}
