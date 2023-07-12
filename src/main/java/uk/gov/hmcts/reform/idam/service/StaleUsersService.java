package uk.gov.hmcts.reform.idam.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.idam.parameter.ParameterResolver;
import uk.gov.hmcts.reform.idam.service.remote.RestClient;
import uk.gov.hmcts.reform.idam.service.remote.StaleUsersRequestParams;
import uk.gov.hmcts.reform.idam.service.remote.requests.RequestBody;
import uk.gov.hmcts.reform.idam.service.remote.requests.UserRoleAssignmentQueryRequest;
import uk.gov.hmcts.reform.idam.service.remote.requests.UserRoleAssignmentQueryRequests;
import uk.gov.hmcts.reform.idam.service.remote.responses.RoleAssignment;
import uk.gov.hmcts.reform.idam.service.remote.responses.RoleAssignmentResponse;
import uk.gov.hmcts.reform.idam.service.remote.responses.StaleUsersResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaleUsersService {

    private final ParameterResolver idamConfig;

    private static final String STALE_USERS_PATH = "/api/v2/staleUsers";
    private static final String PAGE_NUMBER_PARAM = "pageNumber";

    private static final String STALE_USERS_CONTENT_TYPE =
        "application/vnd.uk.gov.hmcts.role-assignment-service.post-assignment-query-request+json;"
            + "charset=UTF-8;"
            + "version=2.0";

    private final StaleUsersRequestParams requestParams;

    private final RestClient client;

    public List<String> retrieveStaleUsers() {
        final Map<String, Object> queryParams = requestParams.getStaleUsersRequestParams();
        queryParams.put(PAGE_NUMBER_PARAM, 1);
        return requestStaleUsers(queryParams);
    }

    public List<String> filterUsersWithRoleAssignments(List<String> staleUsers) {
        Map<String, String> headers = Map.of("Content-Type", STALE_USERS_CONTENT_TYPE);
        var roleAssignmentQuery = UserRoleAssignmentQueryRequest.builder().userIds(staleUsers).build();
        RequestBody body = UserRoleAssignmentQueryRequests.builder().queryRequests(roleAssignmentQuery).build();

        final Response response = client.postRequest(idamConfig.getIdamHost(), STALE_USERS_PATH, headers, body);
        if (response.getStatus() == OK.value()) {
            var assignmentsResponse = response.readEntity(RoleAssignmentResponse.class);
            var assignments = assignmentsResponse
                .getRoleAssignments()
                .stream()
                .map(RoleAssignment::getActorId)
                .toList();
            return staleUsers.stream()
                .filter(userId -> !assignments.contains(userId))
                .toList();
        }
        return List.of();
    }

    private List<String> requestStaleUsers(Map<String, Object> queryParams) {

        final List<String> collected = new LinkedList<>();
        boolean hasMore;

        do {
            final Response response = client.getRequest(idamConfig.getIdamHost(), STALE_USERS_PATH, queryParams);

            if (response.getStatus() == OK.value()) {
                var staleUsersResponse = response.readEntity(StaleUsersResponse.class);
                if (!staleUsersResponse.getStaleUsersResponse().isEmpty()) {
                    collected.addAll(staleUsersResponse.getStaleUsersResponse());
                    staleUsersResponse
                        .getStaleUsersResponse()
                        .stream()
                        .forEach(user -> log.info("Got stale user %s", user));
                }
                hasMore = staleUsersResponse.getMoreRecords() != null && staleUsersResponse.getMoreRecords();
                queryParams.replace(PAGE_NUMBER_PARAM, (int) queryParams.get(PAGE_NUMBER_PARAM) + 1);
            } else {
                hasMore = false;
            }
        } while (hasMore);

        return collected;
    }

}
