package kh.com.acleda.student.rest;

import jakarta.validation.Valid;
import kh.com.acleda.student.payload.CustomerReq;
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
    public Response<?> generateAccessToken(PayPalTokenReq payPalTokenReq){
       return this.payPalService.generateAccessToken(payPalTokenReq);
    }

    @GetMapping (value = "/user_infor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> getUserInfo(@Valid @RequestBody CustomerReq customerReq){
        return this.payPalService.getUserInfo(customerReq);
    }

    @PostMapping(value = "/create_order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> createOrder(@Valid @RequestBody CustomerReq customerReq){
        return this.payPalService.createOrder(customerReq);
    }

    @GetMapping (value = "/order_detail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> showOrderDetails(@Valid @RequestBody CustomerReq customerReq){
        return this.payPalService.showOrderDetails(customerReq);
    }

    @PostMapping(value = "/confirm_payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> confirmPayment(@Valid @RequestBody CustomerReq customerReq){
        return this.payPalService.confirmPayment(customerReq);
    }

    @PostMapping(value = "/authorize", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<?> authorizePayment(@Valid @RequestBody CustomerReq customerReq){
        return this.payPalService.authorizePayment(customerReq);
    }

}