package uk.gov.hmcts.reform.idam.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.idam.exception.IdamAuthTokenGenerationException;
import uk.gov.hmcts.reform.idam.exception.ServiceAuthTokenGenerationException;
import uk.gov.hmcts.reform.idam.exception.UserDetailsGenerationException;
import uk.gov.hmcts.reform.idam.parameter.ParameterResolver;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@EnableScheduling
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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getServiceAuthorization() {
        return serviceAuthToken;
    }

    @Scheduled(fixedRate = 55, timeUnit = TimeUnit.MINUTES)
    private void generateTokens() {
        generateIdamToken();
        generateUserInfo();
        generateServiceToken();
    }

    private void generateIdamToken() {
        try {
            idamClientToken = idamClient.getAccessToken(parameterResolver.getIdamUsername(),
                                                        parameterResolver.getIdamPassword());
        } catch (final Exception exception) {
            log.error("User disposer is unable to generate IDAM token due to error - {}",
                      exception.getMessage(),
                      exception
            );
            throw new IdamAuthTokenGenerationException(
                String.format(
                    "User disposer is unable to generate IDAM token due to error - %s",
                    exception.getMessage()),
                exception);
        }
    }

    private void generateUserInfo() {
        try {
            userInfo = idamClient.getUserInfo(idamClientToken);
        } catch (final Exception exception) {
            log.error("User disposer is unable to generate UserDetails due to error - {}",
                      exception.getMessage(),
                      exception
            );
            throw new UserDetailsGenerationException(
                String.format(
                    "User disposer is unable to generate UserDetails due to error - %s",
                    exception.getMessage()),
                exception);
        }
    }

    private void generateServiceToken() {
        try {
            serviceAuthToken = authTokenGenerator.generate();
        } catch (final Exception exception) {
            log.error("User disposer is unable to generate service auth token due to error - {}",
                      exception.getMessage(),
                      exception
            );
            throw new ServiceAuthTokenGenerationException(
                String.format(
                    "User disposer is unable to generate service auth token due to error - %s",
                    exception.getMessage()),
                exception);
        }
    }
}
