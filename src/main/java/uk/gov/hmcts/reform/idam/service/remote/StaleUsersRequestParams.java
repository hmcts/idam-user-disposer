package uk.gov.hmcts.reform.idam.service.remote;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Data
public class StaleUsersRequestParams {
    private Map<String, Object> staleUsersRequestParams = new ConcurrentHashMap<>();
}
