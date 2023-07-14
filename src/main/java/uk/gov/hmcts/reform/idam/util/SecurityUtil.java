package uk.gov.hmcts.reform.idam.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.idam.parameter.ParameterResolver;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityUtil {

    private final AuthTokenGenerator authTokenGenerator;
    private final IdamClient idamClient;
    private final ParameterResolver parameterResolver;

    private String idamClientToken;
    private UserInfo userInfo;
    private String serviceAuthToken;

    public String getIdamClientToken() {
        return idamClientToken;
    }

    public String getServiceAuthorization() {
        return serviceAuthToken;
    }

}
