package kh.com.acleda.student;

import jakarta.annotation.PostConstruct;
import kh.com.acleda.student.config.AppConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class StudentMsApplication {

    private static final Logger logger = LogManager.getLogger(StudentMsApplication.class);

    private final AppConfiguration appConfiguration;

    public static void main(String[] args) {
        logger.info("Log4j config successfully");
        logger.info("Level Info");
        logger.debug("Level debug");
        logger.warn("Level Warn");
        logger.error("Level Error");
        logger.info("Enjoy your working bro bro");
        SpringApplication.run(StudentMsApplication.class, args);
    }

    @PostConstruct
    public void runOnStartup() {
        appConfiguration.runAtStartup();
    }

}
