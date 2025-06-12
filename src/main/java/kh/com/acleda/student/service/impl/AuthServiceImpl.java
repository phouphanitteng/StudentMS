package kh.com.acleda.student.service.impl;

import kh.com.acleda.student.payload.*;
import kh.com.acleda.student.entity.Student;
import kh.com.acleda.student.entity.StudentPk;
import kh.com.acleda.student.repository.StudentRepository;
import kh.com.acleda.student.service.AuthService;
import kh.com.acleda.student.service.JwtService;
import kh.com.acleda.student.constant.CommonCode;
import kh.com.acleda.student.utils.CommonUtils;
import kh.com.acleda.student.constant.ConstantVariable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final StudentRepository studentRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public Response<?> login(AuthReq authReq) {
        log.debug("Login request: {}", authReq.toString());
        Student student = studentRepository.findByIdEmail(authReq.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Student not found with email, please register first"));
        StudentReq studentReq = StudentReq.builder()
                .email(authReq.getEmail())
                .password(authReq.getPassword())
                .build();
        StudentUserDetails userDetails = new StudentUserDetails(studentReq);
        String token = jwtService.generateToken(studentReq, userDetails);
        student.setLastLogin(LocalDateTime.now());
        studentRepository.save(student);
        log.debug("token: {}", token);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.AUTH_SUCCESS, tokenMap);
    }

    @Override
    @Transactional
    public Response<?> register(RegisterReq registerReq) throws Exception {
         try {
             Optional<Student> studentOpt = studentRepository.findByIdEmail(registerReq.getEmail());
             log.debug("check student exist: " + studentOpt);
             if (studentOpt.isPresent()) {
                 return CommonUtils.modifyRespMsgForFail(CommonCode.BAD_REQUEST, ConstantVariable.STUDENT_EXIST);
             } else {
                 Student stdMapper = modelMapper.map(registerReq, Student.class);

                 String password = CommonUtils.hashSHA256(registerReq.getPassword(), registerReq.getEmail());
                 stdMapper.setPassword(password);
                 stdMapper.setStatus("ACTIVE");
                 stdMapper.setId(StudentPk.builder().studentId(CommonUtils.generateStdId()).email(registerReq.getEmail()).build());
                 stdMapper.setLastLogin(LocalDateTime.now());
                 stdMapper.setDateRegister(LocalDate.now());
                 log.debug("Student Mapper: " + stdMapper);
                 studentRepository.save(stdMapper);
                 log.debug("Student is saved successfully");
                 return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.REGISTER_SUCCESS, stdMapper);
             }
         } catch (Exception e){
             log.error("Student Register error: ", e);
             throw new Exception(e.getMessage());
         }
    }



}
