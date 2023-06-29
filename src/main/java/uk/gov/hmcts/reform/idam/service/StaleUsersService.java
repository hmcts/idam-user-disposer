package uk.gov.hmcts.reform.idam.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.idam.service.remote.RestClient;
import uk.gov.hmcts.reform.idam.service.remote.StaleUsersRequestParams;
import uk.gov.hmcts.reform.idam.service.remote.StaleUsersResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaleUsersService {
    @Value("${idam.api.url}")
    private String host;
    private static final String STALE_USERS_PATH = "/api/v2/staleUsers";
    private static final String PAGE_NUMBER_PARAM = "pageNumber";

    private final StaleUsersRequestParams requestParams;

    private final RestClient client;

    public List<Long> retrieveStaleUsers() {
        final Map<String, Object> params = requestParams.getStaleUsersRequestParams();
        params.put(PAGE_NUMBER_PARAM, 1);
        return makeRequest(params);
    }
    private List<Long> makeRequest(Map<String, Object> params) {

        final List<Long> collected = new LinkedList<>();
        boolean hasMore = true;

        do {
            final Response response = client.getRequest(host, STALE_USERS_PATH, params);

            if (response.getStatus() == HttpStatus.OK.value()) {
                var staleUsersResponse = response.readEntity(StaleUsersResponse.class);
                if (!staleUsersResponse.getStaleUsersResponse().isEmpty()) {
                    collected.addAll(staleUsersResponse.getStaleUsersResponse());
                    staleUsersResponse
                        .getStaleUsersResponse()
                        .stream()
                        .forEach(user -> log.info("Got stale user %s", user));
                }
                hasMore = staleUsersResponse.getMoreRecords() != null && staleUsersResponse.getMoreRecords();
                params.replace(PAGE_NUMBER_PARAM, (int) params.get(PAGE_NUMBER_PARAM) + 1);
            } else {
                hasMore = false;
            }
        } while (hasMore);

        return collected;
    }

}
