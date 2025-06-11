package kh.com.acleda.student.service;

import kh.com.acleda.student.dto.StudentReq;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    String extractUsername(String token);
    String generateToken(StudentReq student, UserDetails userDetails);

    boolean isTokenValid(String jwt, UserDetails userDetails);
}
