package uk.gov.hmcts.reform.idam;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.idam.service.StaleUsersService;

@Component
@RequiredArgsConstructor
public class ApplicationExecutor implements ApplicationRunner {

    private final StaleUsersService service;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        service.retrieveStaleUsers();
    }

}
