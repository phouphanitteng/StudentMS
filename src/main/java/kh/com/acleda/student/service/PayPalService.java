package kh.com.acleda.student.service;

import kh.com.acleda.student.payload.CustomerReq;
import kh.com.acleda.student.payload.PayPalTokenReq;
import kh.com.acleda.student.payload.Response;
import org.springframework.stereotype.Service;

@Service
public interface PayPalService {
    Response<?> generateAccessToken(PayPalTokenReq payPalTokenReq);

    Response<?> getUserInfo(CustomerReq customerReq);

    Response<?> createOrder(CustomerReq customerReq);

    Response<?> showOrderDetails(CustomerReq customerReq);

    Response<?> confirmPayment(CustomerReq customerReq);

    Response<?> authorizePayment(CustomerReq customerReq);
}
