package kh.com.acleda.student.utils;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import kh.com.acleda.student.dto.Response;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
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


@Service
@RequiredArgsConstructor
public class InterfaceAdapter {

    private static final Logger log = LogManager.getLogger(InterfaceAdapter.class);

    public Response<?> callExternalService(String baseUrl, String methodType, JSONObject requestHeader, String requestBody) {

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
                .exchangeToMono(Mono::just)
                .block();

        if(!ObjectUtils.isEmpty(clientResponse)){

            int statusCode = clientResponse.statusCode().value();
            String responseBody = clientResponse.bodyToMono(String.class).block();

            log.info("HTTP Status Code: {}", statusCode);
            log.info("Response Body: {}", responseBody);

            return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.EXTERNAL_SUC, responseBody);
        } else return CommonUtils.generateResponse(CommonCode.EXTERNAL_ERROR);
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
                .exchangeToMono(Mono::just)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(this::shouldRetry)
                )
                .doFinally(signal -> log.warn("Retrying due to: {}", signal))
                .block();

        if(!ObjectUtils.isEmpty(clientResponse)){

            int statusCode = clientResponse.statusCode().value();
            String responseBody = clientResponse.bodyToMono(String.class).block();

            log.info("HTTP Status Code: {}", statusCode);
            log.info("Response Body: {}", responseBody);

            return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.EXTERNAL_SUC, responseBody);
        } else return CommonUtils.generateResponse(CommonCode.EXTERNAL_ERROR);
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
