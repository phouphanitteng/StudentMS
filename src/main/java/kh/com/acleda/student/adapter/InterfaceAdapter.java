package kh.com.acleda.student.adapter;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutException;
import io.netty.handler.timeout.WriteTimeoutHandler;
import kh.com.acleda.student.payload.Header;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.PrematureCloseException;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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

        log.info("connection timeout: {}", ConstantVariable.CONNECTED_TIMEOUT);
        log.info("Read timeout: {}", ConstantVariable.READ_TIMEOUT);
        log.info("Write timeout: {}", ConstantVariable.WRITE_TIMEOUT);

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConstantVariable.CONNECTED_TIMEOUT)
                .responseTimeout(Duration.ofMillis(ConstantVariable.CONNECTED_TIMEOUT))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(ConstantVariable.READ_TIMEOUT, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(ConstantVariable.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)));

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

        log.info("connection timeout: {}", ConstantVariable.CONNECTED_TIMEOUT);
        log.info("Read timeout: {}", ConstantVariable.READ_TIMEOUT);
        log.info("Write timeout: {}", ConstantVariable.WRITE_TIMEOUT);

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConstantVariable.CONNECTED_TIMEOUT)
                .responseTimeout(Duration.ofMillis(ConstantVariable.CONNECTED_TIMEOUT))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(ConstantVariable.READ_TIMEOUT, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(ConstantVariable.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)));

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
                        Retry.backoff(ConstantVariable.RETRY_ATTEMPTS, Duration.ofSeconds(ConstantVariable.DURATION_SECOND))
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

    public Response<?> callServiceDynamicType(String baseUrl, String methodType, Header header , JSONObject requestHeader, String requestBody) {

        log.info("Final URL: {}", baseUrl);
        log.info("Method Type: {}", methodType);
        log.info("Request Header: {}", requestHeader.toString());
        log.info("Request Body: {}", requestBody);
        log.info("Connection timeout: {}", ConstantVariable.CONNECTED_TIMEOUT);
        log.info("Read timeout: {}", ConstantVariable.READ_TIMEOUT);
        log.info("Write timeout: {}", ConstantVariable.WRITE_TIMEOUT);

        // Setup WebClient timeouts
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConstantVariable.CONNECTED_TIMEOUT)
                .responseTimeout(Duration.ofMillis(ConstantVariable.CONNECTED_TIMEOUT))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(ConstantVariable.READ_TIMEOUT, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(ConstantVariable.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)));

       WebClient.Builder clientBuilder =  WebClient.builder()
               .clientConnector(new ReactorClientHttpConnector(httpClient));

       if(StringUtils.hasText(header.getAuthorizeType())){
           if(ConstantVariable.AUTHORIZED_BASIC.equals(header.getAuthorizeType())){
               clientBuilder =  WebClient.builder()
                       .defaultHeaders(headers -> headers.setBasicAuth(header.getUsername(), header.getPassword()));

           } else if(ConstantVariable.AUTHORIZED_BEARER.equals(header.getAuthorizeType())) {
               clientBuilder =  WebClient.builder()
                       .defaultHeaders(headers -> headers.setBearerAuth(header.getAuthorization()));
           }
       }

       WebClient webClient = clientBuilder.build();

        AtomicReference<String> responseBody = new AtomicReference<>("");

        // Determine and parse Content-Type
        String contentType = requestHeader.optString(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        MediaType mediaType = MediaType.parseMediaType(contentType);
        log.info("Parsed Content-Type: {}", mediaType);

        // Build request
        WebClient.RequestBodySpec requestSpec = webClient
                .method(HttpMethod.valueOf(methodType.toUpperCase()))
                .uri(baseUrl)
                .headers(httpHeaders -> {
                    requestHeader.keySet().forEach(key -> {
                        Object value = requestHeader.get(key);
                        httpHeaders.add(key, String.valueOf(value));
                    });
                })
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .contentType(mediaType);

        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
            try {
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                JSONObject formJson = new JSONObject(requestBody);
                formJson.keySet().forEach(key -> formData.add(key, formJson.getString(key)));
                log.info("Form Data: {}", formData);
                requestSpec = (WebClient.RequestBodySpec) requestSpec.body(BodyInserters.fromFormData(formData));
            } catch (Exception e) {
                log.error("Failed to parse form body: {}", e.getMessage(), e);
                throw new RuntimeException("BodyInserters parsing error");
            }
        } else {
            requestSpec = (WebClient.RequestBodySpec) requestSpec.bodyValue(requestBody);
        }

        ClientResponse clientResponse = requestSpec
                .exchangeToMono(response -> response.bodyToMono(String.class).map(body -> {
                    log.info("HTTP Status Code: {}", response.statusCode().value());
                    log.info("Response Body: {}", body);
                    responseBody.set(body);
                    return response;
                }))
                .doOnError(e -> {
                    log.error("Error during WebClient request", e);
                    if (e instanceof ReadTimeoutException) {
                        log.error("Read timeout occurred");
                         throw new RuntimeException(baseUrl+" :Read timeout occurred");
                    } else if (e instanceof WriteTimeoutException) {
                        log.error("Write timeout occurred");
                        throw new RuntimeException(baseUrl+" :Write timeout occurred");
                    } else if (e instanceof TimeoutException) {
                        log.error("Timeout occurred");
                        throw new RuntimeException("Timeout occurred");
                    } else if (e instanceof PrematureCloseException) {
                        log.error("Connection closed prematurely");
                        throw new RuntimeException("Connection closed prematurely");
                    } else if(e instanceof WebClientRequestException){
                        throw new RuntimeException("WebClientRequestException closed prematurely");
                    } else if(e instanceof WebClientResponseException){
                        throw new RuntimeException("WebClientResponseException closed prematurely");
                    } else throw new RuntimeException(e.getMessage());
                })
                .block();

        return clientResponse != null && !responseBody.get().isEmpty()
                ? CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.EXTERNAL_SUC, responseBody.get())
                : CommonUtils.generateResponse(CommonCode.EXTERNAL_ERROR);
    }

    public Response<?> callServiceDynamicTypeRetry(String baseUrl, String methodType, Header header , JSONObject requestHeader, String requestBody) {

        log.info("Final URL: {}", baseUrl);
        log.info("Method Type: {}", methodType);
        log.info("Request Header: {}", requestHeader.toString());
        log.info("Request Body: {}", requestBody);
        log.info("Connection timeout: {}", ConstantVariable.CONNECTED_TIMEOUT);
        log.info("Read timeout: {}", ConstantVariable.READ_TIMEOUT);
        log.info("Write timeout: {}", ConstantVariable.WRITE_TIMEOUT);

        // Setup WebClient timeouts
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConstantVariable.CONNECTED_TIMEOUT)
                .responseTimeout(Duration.ofMillis(ConstantVariable.CONNECTED_TIMEOUT))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(ConstantVariable.READ_TIMEOUT, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(ConstantVariable.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)));

        WebClient.Builder clientBuilder =  WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));

        if(StringUtils.hasText(header.getAuthorizeType())){
            if(ConstantVariable.AUTHORIZED_BASIC.equals(header.getAuthorizeType())){
                clientBuilder =  WebClient.builder()
                        .defaultHeaders(headers -> headers.setBasicAuth(header.getUsername(), header.getPassword()));

            } else if(ConstantVariable.AUTHORIZED_BEARER.equals(header.getAuthorizeType())) {
                clientBuilder =  WebClient.builder()
                        .defaultHeaders(headers -> headers.setBearerAuth(header.getAuthorization()));
            }
        }

        WebClient webClient = clientBuilder.build();

        AtomicReference<String> responseBody = new AtomicReference<>("");

        // Determine and parse Content-Type
        String contentType = requestHeader.optString(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        MediaType mediaType = MediaType.parseMediaType(contentType);
        log.info("Parsed Content-Type: {}", mediaType);

        // Build request
        WebClient.RequestBodySpec requestSpec = webClient
                .method(HttpMethod.valueOf(methodType.toUpperCase()))
                .uri(baseUrl)
                .headers(httpHeaders -> {
                    requestHeader.keySet().forEach(key -> {
                        Object value = requestHeader.get(key);
                        httpHeaders.add(key, String.valueOf(value));
                    });
                })
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .contentType(mediaType);

        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
            try {
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                JSONObject formJson = new JSONObject(requestBody);
                formJson.keySet().forEach(key -> formData.add(key, formJson.getString(key)));
                log.info("Form Data: {}", formData);
                requestSpec = (WebClient.RequestBodySpec) requestSpec.body(BodyInserters.fromFormData(formData));
            } catch (Exception e) {
                log.error("Failed to parse form body: {}", e.getMessage(), e);
                throw new RuntimeException("BodyInserters parsing error");
            }
        } else {
            requestSpec = (WebClient.RequestBodySpec) requestSpec.bodyValue(requestBody);
        }

        ClientResponse clientResponse = requestSpec
                .exchangeToMono(response -> response.bodyToMono(String.class).map(body -> {
                    log.info("HTTP Status Code: {}", response.statusCode().value());
                    log.info("Response Body: {}", body);
                    responseBody.set(body);
                    return response;
                }))
                .retryWhen(
                        Retry.backoff(ConstantVariable.RETRY_ATTEMPTS, Duration.ofSeconds(ConstantVariable.DURATION_SECOND))
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

}
