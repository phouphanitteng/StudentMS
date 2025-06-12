package kh.com.acleda.student.service;

import kh.com.acleda.student.payload.ApiRequest;
import kh.com.acleda.student.payload.UserInfoRequest;
import kh.com.acleda.student.payload.PayPalTokenReq;
import kh.com.acleda.student.payload.Response;
import org.springframework.stereotype.Service;

@Service
public interface PayPalService {
    Response<?> generateAccessToken(PayPalTokenReq payPalTokenReq);

    Response<?> getUserInfo(ApiRequest<UserInfoRequest> apiRequest);

    Response<?> createOrder(UserInfoRequest userInfoRequest);

    Response<?> showOrderDetails(UserInfoRequest userInfoRequest);

    Response<?> confirmPayment(UserInfoRequest userInfoRequest);

    Response<?> authorizePayment(UserInfoRequest userInfoRequest);
}
