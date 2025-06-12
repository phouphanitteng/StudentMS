package kh.com.acleda.student.service.impl;

import kh.com.acleda.student.payload.CustomerReq;
import kh.com.acleda.student.payload.PayPalTokenReq;
import kh.com.acleda.student.payload.Response;
import kh.com.acleda.student.service.PayPalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayPalServiceImpl implements PayPalService {

    @Override
    public Response<?> generateAccessToken(PayPalTokenReq payPalTokenReq) {
        log.info("slf4j");
        return null;
    }

    @Override
    public Response<?> getUserInfo(CustomerReq customerReq) {
        return null;
    }

    @Override
    public Response<?> createOrder(CustomerReq customerReq) {
        return null;
    }

    @Override
    public Response<?> showOrderDetails(CustomerReq customerReq) {
        return null;
    }

    @Override
    public Response<?> confirmPayment(CustomerReq customerReq) {
        return null;
    }

    @Override
    public Response<?> authorizePayment(CustomerReq customerReq) {
        return null;
    }
}
