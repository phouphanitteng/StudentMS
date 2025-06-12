package kh.com.acleda.student.utils;

import kh.com.acleda.student.constant.CommonCode;
import kh.com.acleda.student.payload.Response;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
@Slf4j
public class CommonUtils {

    private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
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

    public static String generateStdId(){
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        String random = String.format("%06d", new Random().nextInt(10000)); // 4-digit random number
        return "STD" + timestamp + random;
    }

    public static String hashSHA256(String pText, String pSalt) {
        String pTextSalt = pText + pSalt;
        String pHashedText = "";
        byte[] ptextSaltbyte;
        byte[] hashbyte;
        try {
            MessageDigest msgdigest = MessageDigest.getInstance("SHA-256");
            ptextSaltbyte = pTextSalt.getBytes(StandardCharsets.UTF_8);
            msgdigest.reset();
            msgdigest.update(ptextSaltbyte);
            hashbyte = msgdigest.digest();
            pHashedText = toHexString(hashbyte);
        } catch (NoSuchAlgorithmException n) {
            log.error("NoSuchAlgorithmException ex"+ n);
        }
        return pHashedText;
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte value : b) {
            int c = (value >>> 4) & 0xf;
            sb.append(HEX[c]);
            c = (value & 0xf);
            sb.append(HEX[c]);
        }
        return sb.toString();
    }


}
