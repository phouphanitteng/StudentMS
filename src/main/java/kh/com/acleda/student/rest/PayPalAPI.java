package kh.com.acleda.student.rest;

import jakarta.validation.Valid;
import kh.com.acleda.student.payload.ApiRequest;
import kh.com.acleda.student.payload.UserInfoRequest;
import kh.com.acleda.student.payload.PayPalTokenReq;
import kh.com.acleda.student.payload.Response;
import kh.com.acleda.student.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal/")
@RequiredArgsConstructor
public class PayPalAPI {

    private final PayPalService payPalService;

    @PostMapping(value = "/access_token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> generateAccessToken(@Valid @RequestBody PayPalTokenReq payPalTokenReq){
       return this.payPalService.generateAccessToken(payPalTokenReq);
    }

    @GetMapping (value = "/user_infor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> getUserInfo(@Valid @RequestBody ApiRequest<UserInfoRequest> apiRequest){
        return this.payPalService.getUserInfo(apiRequest);
    }

    @PostMapping(value = "/create_order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> createOrder(@Valid @RequestBody UserInfoRequest userInfoRequest){
        return this.payPalService.createOrder(userInfoRequest);
    }

    @GetMapping (value = "/order_detail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> showOrderDetails(@Valid @RequestBody UserInfoRequest userInfoRequest){
        return this.payPalService.showOrderDetails(userInfoRequest);
    }

    @PostMapping(value = "/confirm_payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> confirmPayment(@Valid @RequestBody UserInfoRequest userInfoRequest){
        return this.payPalService.confirmPayment(userInfoRequest);
    }

    @PostMapping(value = "/authorize", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> authorizePayment(@Valid @RequestBody UserInfoRequest userInfoRequest){
        return this.payPalService.authorizePayment(userInfoRequest);
    }

}