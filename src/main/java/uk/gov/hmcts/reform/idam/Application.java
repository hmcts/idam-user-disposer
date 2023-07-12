package uk.gov.hmcts.reform.idam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.idam"})
public class Application implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting the Idam-Disposer job..");
    }

    public static void main(final String[] args) {
        final ApplicationContext context = SpringApplication.run(Application.class);
        SpringApplication.exit(context);
    }
}
