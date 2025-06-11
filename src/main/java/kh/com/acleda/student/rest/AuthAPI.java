package kh.com.acleda.student.rest;

import jakarta.validation.Valid;
import kh.com.acleda.student.dto.AuthReq;
import kh.com.acleda.student.dto.Response;
import kh.com.acleda.student.dto.StudentReq;
import kh.com.acleda.student.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthAPI {

    private final AuthService authService;

    @PostMapping("/login")
    public Response<?> login(@RequestBody @Valid AuthReq authReq){
        return this.authService.login(authReq);
    }

    @PostMapping("/register")
    public Response<?> register(@RequestBody @Valid StudentReq authReq) throws Exception {
        return this.authService.register(authReq);
    }

}
