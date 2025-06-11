package kh.com.acleda.student.utils;

import kh.com.acleda.student.dto.Response;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@Getter
@Setter
public class CommonUtils {

    protected static Map<String, String> commonProperties = null;
    public static String getCommonProperties(String commonKey) {
        return commonProperties.get(commonKey);
    }
    public static void initializeCommonProperties(Map<String, String> commonValues) {
        commonProperties = commonValues;
    }

    public static Response<?> generateRespForSuccess(Object data, String message){
        return Response.builder()
                .responseCode(200)
                .errorCode("")
                .errorMessage(message)
                .data(data)
                .build();
    }

    public static Response<?> generateRespForFailure(int code, String errorCode, String message){
        return Response.builder()
                .responseCode(code)
                .errorCode(errorCode)
                .errorMessage(message)
                .data(null)
                .build();
    }
    public static Response<?> modifyRespMsgForSuccess(CommonCode code, String message, Object data){
        return new Response<>(
                code.getResponseCode(),
                code.getErrorCode(),
                message,
                data);
    }

    public static Response<?> modifyRespMsgForFail(CommonCode code, String message){
        return new Response<>(
                code.getResponseCode(),
                code.getErrorCode(),
                code.getMessage(),
                null);
    }

    public static Response<?> generateResponse(CommonCode code){
        return new Response<>(
                code.getResponseCode(),
                code.getErrorCode(),
                code.getMessage(),
                null);
    }

}
