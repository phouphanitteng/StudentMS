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

    private final AppConfiguration appConfiguration;

    public static void main(String[] args) {
        SpringApplication.run(StudentMsApplication.class, args);
    }

    @PostConstruct
    public void runOnStartup() {
        appConfiguration.runAtStartup();
    }

}
