package uk.gov.hmcts.reform.idam.service;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.idam.service.remote.RestClient;
import uk.gov.hmcts.reform.idam.service.remote.StaleUsersRequestParams;
import uk.gov.hmcts.reform.idam.service.remote.StaleUsersResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StaleUsersServiceTest {
    @Mock
    RestClient client;

    @Mock
    private StaleUsersRequestParams requestParams;

    @InjectMocks
    private StaleUsersService service;

    @Test
    void shouldMakeARequestOnRetrieveStaleUsers() {

        Response firstResponse = buildMockResponse(
            "{\"staleUsers\":[1, 2],\"moreRecords\":true}",
            new StaleUsersResponse(List.of(1L, 2L), true)
        );
        Response secondResponse = buildMockResponse(
            "{\"staleUsers\":[1, 2],\"moreRecords\":false}",
            new StaleUsersResponse(List.of(3L, 4L), false)
        );

        when(client.getRequest(nullable(String.class), anyString(), anyMap()))
            .thenReturn(firstResponse)
            .thenReturn(secondResponse);

        List<Long> staleUsers = service.retrieveStaleUsers();
        assertThat(staleUsers).hasSize(4);
        verify(client, times(2)).getRequest(nullable(String.class), anyString(), anyMap());

    }

    private Response buildMockResponse(String msg, StaleUsersResponse entity) {
        Response resp = mock(Response.class);
        when(resp.readEntity(StaleUsersResponse.class)).thenReturn(entity);
        when(resp.getStatus()).thenReturn(HttpStatus.OK.value());
        return resp;
    }
}
