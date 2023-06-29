package uk.gov.hmcts.reform.idam.service.remote;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.idam.util.SecurityUtil;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.glassfish.jersey.client.ClientProperties.CONNECT_TIMEOUT;
import static org.glassfish.jersey.client.ClientProperties.READ_TIMEOUT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class RestClientTest {
    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private RestConfig restConfig;

    @Test
    void getRequest() {
        final Client client = mock(Client.class);
        final WebTarget webTarget = mock(WebTarget.class);
        final Invocation.Builder builder = mock(Invocation.Builder.class);
        final Response response = mock(Response.class);
        when(restConfig.getAuthorisationHeaderName()).thenReturn("Authorization");
        when(restConfig.getServiceAuthorisationHeaderName()).thenReturn("ServiceAuthorization");

        final RestClient restClient = new RestClient(securityUtil, restConfig);

        setField(restClient, "client", client);

        doReturn("Bearer 1234").when(securityUtil).getIdamClientToken();
        doReturn("Service 5678").when(securityUtil).getServiceAuthorization();
        when(client.target(any(String.class))).thenReturn(webTarget);
        when(webTarget.path(any(String.class))).thenReturn(webTarget);
        when(webTarget.queryParam(any(String.class), any(String.class))).thenReturn(webTarget);

        when(webTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
        when(builder.header("Authorization", "Service 5678")).thenReturn(builder);
        when(builder.header("ServiceAuthorization", "Bearer 1234")).thenReturn(builder);
        when(builder.get()).thenReturn(response);

        final Response getResponse = restClient.getRequest(
            "http://localhost:9090",
            "/delete",
            Map.of("size", 100, "sort", "log-timestamp")
        );

        verify(webTarget, times(2)).queryParam(any(String.class), any(Object.class));
        verify(webTarget, times(1)).request(MediaType.APPLICATION_JSON_TYPE);
        verify(builder, times(1)).get();
    }

    @Test
    void shouldGetClient() {
        long timeout = 60_000;
        when(restConfig.getReadTimeout()).thenReturn(timeout);
        when(restConfig.getConnectTimeout()).thenReturn(timeout);

        final RestClient restClient = new RestClient(securityUtil, restConfig);

        var client = restClient.getClient();
        assertThat(client.getConfiguration().getProperty(READ_TIMEOUT)).isEqualTo(timeout);
        assertThat(client.getConfiguration().getProperty(CONNECT_TIMEOUT)).isEqualTo(timeout);
    }

}
