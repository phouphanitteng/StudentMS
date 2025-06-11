package kh.com.acleda.student.service;

import kh.com.acleda.student.payload.AuthReq;
import kh.com.acleda.student.payload.RegisterReq;
import kh.com.acleda.student.payload.Response;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    Response<?> login(AuthReq authReq);

    Response<?> register(RegisterReq registerReq) throws Exception;
}
