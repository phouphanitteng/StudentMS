package kh.com.acleda.student.config;

import kh.com.acleda.student.repository.StudentRepository;
import kh.com.acleda.student.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    private static final Logger logger = LogManager.getLogger(AppConfiguration.class);

    private final CommonProperties commonProperties;
    private final StudentRepository studentRepository;
    public void runAtStartup() {
        CommonUtils.initializeCommonProperties(commonProperties.getCommon());
        if (logger.isInfoEnabled())
            logger.info("Core is booting...");
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> (UserDetails) studentRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("student email not found"));
    }
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new DaoAuthenticationProvider(userDetailsService());
    }


}
