package uk.gov.hmcts.reform.idam.service.remote;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
public class StaleUsersRequestParams {
    private Map<String, Object> staleUsersRequestParams;
}
