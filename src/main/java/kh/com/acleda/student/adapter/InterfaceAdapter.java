package kh.com.acleda.student.adapter;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import kh.com.acleda.student.payload.Response;
import kh.com.acleda.student.constant.CommonCode;
import kh.com.acleda.student.utils.CommonUtils;
import kh.com.acleda.student.constant.ConstantVariable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


@Service
@RequiredArgsConstructor
@Slf4j
public class InterfaceAdapter {

    public Response<?> callExternalService(String baseUrl, String methodType, JSONObject requestHeader, String requestBody) {

        log.info("final url: {}", baseUrl);
        log.info("Method Type: {}", methodType);
        log.info("request Header: {}", requestHeader.toString());
        log.info("request Body: {}", requestBody);

        // configure timeout
        int connectedTimeout = 5000;
        int readTimeout = 5000;
        int writeTimeout = 5000;

        log.info("connection timeout: {}", connectedTimeout);
        log.info("Read timeout: {}", readTimeout);
        log.info("Write timeout: {}", writeTimeout);

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectedTimeout)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS)));

        WebClient client = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        AtomicReference<String> responseBody = new AtomicReference<>("");

        ClientResponse clientResponse = client
                .method(HttpMethod.valueOf(methodType))
                .uri(baseUrl)
                .headers(httpHeaders -> {
                    requestHeader.keySet().forEach(key -> {
                        httpHeaders.add(key, requestHeader.getString(key));
                    });
                })
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(requestBody)
                .exchangeToMono(response ->
                        response.bodyToMono(String.class).map(body -> {
                            log.info("HTTP Status Code: {}", response.statusCode().value());
                            log.info("Response Body: {}", body);
                            responseBody.set(body);
                            return response;
                        }))
                .block();


        return clientResponse != null && !responseBody.get().isEmpty()
                ? CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.EXTERNAL_SUC, responseBody.get())
                : CommonUtils.generateResponse(CommonCode.EXTERNAL_ERROR);

    }


    public Response<?> callExternalServiceRetry(String baseUrl, String methodType, JSONObject requestHeader, String requestBody) {

        log.info("Base url: {}", baseUrl);
        log.info("Method Type: {}", methodType);
        log.info("request Header: {}", requestHeader.toString());
        log.info("request Body: {}", requestBody);

        // configure timeout
        int connectedTimeout = 5000;
        int readTimeout = 5000;
        int writeTimeout = 5000;

        log.info("connection timeout: {}", connectedTimeout);
        log.info("Read timeout: {}", readTimeout);
        log.info("Write timeout: {}", writeTimeout);

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectedTimeout)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS)));

        WebClient client = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        AtomicReference<String> responseBody = new AtomicReference<>("");

        ClientResponse clientResponse = client
                .method(HttpMethod.valueOf(methodType))
                .uri(baseUrl)
                .headers(httpHeaders -> {
                    requestHeader.keySet().forEach(key -> {
                        httpHeaders.add(key, requestHeader.getString(key));
                    });
                })
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(requestBody)
                .exchangeToMono(response -> {
                    if (response.statusCode().isError()) {
                        return response.createException().flatMap(Mono::error);
                    } else {
                        return response.bodyToMono(String.class).map(body -> {
                            responseBody.set(body);
                            log.info("HTTP Status Code: {}", response.statusCode().value());
                            log.info("Response Body: {}", body);
                            return response;
                        });
                    }
                })
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(10))
                                .filter(this::shouldRetry)
                                .doBeforeRetry(retrySignal ->
                                        log.warn("Retry attempt #{} due to {}",
                                                retrySignal.totalRetries() + 1,
                                                retrySignal.failure().getMessage())
                                )
                                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                        retrySignal.failure()
                                )
                )
                .block();

        return clientResponse != null && !responseBody.get().isEmpty()
                ? CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.EXTERNAL_SUC, responseBody.get())
                : CommonUtils.generateResponse(CommonCode.EXTERNAL_ERROR);

    }
    private boolean shouldRetry(Throwable throwable) {
        if (throwable instanceof WebClientRequestException) {
            return true;
        } else if (throwable instanceof WebClientResponseException ex) {
            int statusCode = ex.getStatusCode().value();
            return statusCode >= 400 && statusCode < 600;
        }
        return false;
    }



}
