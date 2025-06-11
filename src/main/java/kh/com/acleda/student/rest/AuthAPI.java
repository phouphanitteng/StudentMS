package kh.com.acleda.student.rest;

import jakarta.validation.Valid;
import kh.com.acleda.student.payload.AuthReq;
import kh.com.acleda.student.payload.RegisterReq;
import kh.com.acleda.student.payload.Response;
import kh.com.acleda.student.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthAPI {

    private final AuthService authService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> login(@RequestBody @Valid AuthReq authReq){
        return this.authService.login(authReq);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> register(@RequestBody @Valid RegisterReq registerReq) throws Exception {
        return this.authService.register(registerReq);
    }

}
