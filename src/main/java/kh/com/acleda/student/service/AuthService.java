package kh.com.acleda.student.service;

import kh.com.acleda.student.dto.AuthReq;
import kh.com.acleda.student.dto.RegisterReq;
import kh.com.acleda.student.dto.Response;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    Response<?> login(AuthReq authReq);

    Response<?> register(RegisterReq registerReq) throws Exception;
}
