package kh.com.acleda.student.service.impl;

import kh.com.acleda.student.dto.AuthReq;
import kh.com.acleda.student.dto.Response;
import kh.com.acleda.student.dto.StudentReq;
import kh.com.acleda.student.dto.StudentUserDetails;
import kh.com.acleda.student.entity.Student;
import kh.com.acleda.student.repository.StudentRepository;
import kh.com.acleda.student.service.AuthService;
import kh.com.acleda.student.service.JwtService;
import kh.com.acleda.student.utils.CommonCode;
import kh.com.acleda.student.utils.CommonUtils;
import kh.com.acleda.student.utils.ConstantVariable;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LogManager.getLogger(AuthServiceImpl.class);

    private final StudentRepository studentRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    @Override
    public Response<?> login(AuthReq authReq) {
        log.debug("Login request: {}", authReq.toString());
        StudentReq studentReq = StudentReq.builder()
                .email(authReq.getEmail())
                .password(authReq.getPassword())
                .build();
        StudentUserDetails userDetails = new StudentUserDetails(studentReq);
        String token = jwtService.generateToken(studentReq, userDetails);
        log.debug("token: {}", token);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.AUTH_SUCCESS, tokenMap);
    }

    @Override
    @Transactional
    public Response<?> register(StudentReq studentReq) throws Exception {
         try {
             Optional<Student> studentOpt = studentRepository.findByEmail(studentReq.getEmail());
             log.debug("check student exist: " + studentOpt);
             if (studentOpt.isPresent()) {
                 return CommonUtils.modifyRespMsgForFail(CommonCode.BAD_REQUEST, ConstantVariable.STUDENT_EXIST);
             } else {
                 Student stdMapper = modelMapper.map(studentReq, Student.class);

                 String password = hashSHA256(studentReq.getPassword(), studentReq.getEmail());
                 stdMapper.setPassword(password);
                 stdMapper.setStatus("ACTIVE");

                 log.debug("Student Mapper: " + stdMapper);
                 studentRepository.save(stdMapper);
                 log.debug("Student is saved successfully");
                 return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.REGISTER_SUCCESS, stdMapper);
             }
         } catch (Exception e){
             log.error("Student Register error: " + e);
             throw new Exception(e.getMessage());
         }
    }


    public String hashSHA256(String pText, String pSalt) {
        String pTextSalt = pText + pSalt;
        String pHashedText = "";
        byte[] ptextSaltbyte;
        byte[] hashbyte;
        try {
            MessageDigest msgdigest = MessageDigest.getInstance("SHA-256");
            ptextSaltbyte = pTextSalt.getBytes(StandardCharsets.UTF_8);
            msgdigest.reset();
            msgdigest.update(ptextSaltbyte);
            hashbyte = msgdigest.digest();
            pHashedText = toHexString(hashbyte);
        } catch (NoSuchAlgorithmException n) {
            log.error("NoSuchAlgorithmException ex"+ n);
        }
        return pHashedText;
    }

    public String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte value : b) {
            int c = (value >>> 4) & 0xf;
            sb.append(HEX[c]);
            c = (value & 0xf);
            sb.append(HEX[c]);
        }
        return sb.toString();
    }
}
