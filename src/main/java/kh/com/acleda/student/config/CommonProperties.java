package kh.com.acleda.student.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Configuration
@ConfigurationProperties("app")
public class CommonProperties {

    private final Map<String, String> common = new HashMap<>();

}
