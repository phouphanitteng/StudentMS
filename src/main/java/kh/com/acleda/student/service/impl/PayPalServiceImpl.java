package kh.com.acleda.student.service.impl;

import kh.com.acleda.student.adapter.InterfaceAdapter;
import kh.com.acleda.student.payload.*;
import kh.com.acleda.student.service.PayPalService;
import kh.com.acleda.student.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayPalServiceImpl implements PayPalService {

    private final InterfaceAdapter interfaceAdapter;

    @Override
    public Response<?> generateAccessToken(PayPalTokenReq payPalTokenReq) {
        log.debug("Request to generate PayPal access token initiated: {}", payPalTokenReq);

        String baseUrl = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
        String methodType = "POST";

        JSONObject requestHeader = new JSONObject();
        requestHeader.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        Header header = CommonUtils.obtainHeaderBasicAuth(payPalTokenReq.getClientId(), payPalTokenReq.getClientSecret());
        log.debug("obtain header request: " + header.toString());
        JSONObject requestBody = new JSONObject();
        requestBody.put("grant_type", "client_credentials");
        requestBody.put("ignoreCache", "true");
        requestBody.put("return_authn_schemes", "true");
        requestBody.put("return_client_metadata", "true");
        requestBody.put("return_unconsented_scopes", "true");

        return this.interfaceAdapter.callServiceDynamicType(
                baseUrl,
                methodType,
                header,
                requestHeader,
                requestBody.toString()
        );
    }


    @Override
    public Response<?> getUserInfo(ApiRequest<UserInfoRequest> apiRequest) {
        log.debug("apiRequest: {}", apiRequest.getApiReqBody());
        Header header = CommonUtils.obtainHeaderBearerAuth(apiRequest.getApiReqHeader().getAuthorization());
        String baseUrl = "https://api-m.sandbox.paypal.com/v1/identity/oauth2/userinfo?schema="+apiRequest.getApiReqBody().getUserId();
        String methodType = "GET";
        log.debug("Header: {}", header);
        return this.interfaceAdapter.callServiceDynamicType(
                baseUrl,
                methodType,
                header,
                new JSONObject(),
                apiRequest.getApiReqBody().getUserId()
        );
    }

    @Override
    public Response<?> createOrder(UserInfoRequest userInfoRequest) {
        return null;
    }

    @Override
    public Response<?> showOrderDetails(UserInfoRequest userInfoRequest) {
        return null;
    }

    @Override
    public Response<?> confirmPayment(UserInfoRequest userInfoRequest) {
        return null;
    }

    @Override
    public Response<?> authorizePayment(UserInfoRequest userInfoRequest) {
        return null;
    }
}
