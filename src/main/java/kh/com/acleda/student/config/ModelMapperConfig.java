package kh.com.acleda.student.config;

import kh.com.acleda.student.dto.StudentResp;
import kh.com.acleda.student.entity.Student;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addMappings(new PropertyMap<Student, StudentResp>() {
            @Override
            protected void configure() {
                map().setStudentId(source.getId().getStudentId());
                map().setEmail(source.getId().getEmail());
            }
        });
        return modelMapper;
    }
}
