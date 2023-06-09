package uk.gov.hmcts.reform.idam;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class Application implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting the Idam-Disposer job.");
    }
}
