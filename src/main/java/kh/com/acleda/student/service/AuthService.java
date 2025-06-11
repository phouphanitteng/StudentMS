package kh.com.acleda.student.service;

import jakarta.validation.Valid;
import kh.com.acleda.student.dto.AuthReq;
import kh.com.acleda.student.dto.Response;
import kh.com.acleda.student.dto.StudentReq;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    Response<?> login(AuthReq authReq);

    Response<?> register(@Valid StudentReq authReq) throws Exception;
}
