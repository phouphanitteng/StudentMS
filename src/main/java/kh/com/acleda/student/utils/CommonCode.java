package kh.com.acleda.student.utils;

public enum CommonCode {
    SUCCESS(0, null,"Success"),
    SERVER_ERROR(1, "999","Internal Server Error"),
    EXTERNAL_ERROR(1, "995","External Server Error"),
    RECIPIENT_TIMEOUT(1, "998","Recipient Timeout"),
    SERVICE_ERROR(1, "997","Service is unavailable"),
    SOMETHING_WRONG(1, "996","Something went wrong"),
    AUTH_FAILED(1, "001","Authentication error"),
    VALIDATE_ERR(1, "002","Validation Error"),
    INVALID_QR(1, "003","Invalid QR Code"),
    INVALID_AMT(1, "004","Invalid Amount"),
    INVALID_CCY(1, "005","Invalid Currency"),
    INVALID_MERCHANT(1, "006","Invalid Merchant"),
    INVALID_PAYMENT_REF(1, "007","Invalid Payment Reference"),
    INVALID_CD_ACC(1, "008","Invalid Credit Account Number"),
    QR_NOT_ACCEPTED(1, "010","QR Code is not available for payment"),
    QR_CCY_NOT_SUPPORT(1, "011","QR Code Currency is not support"),
    MCOVER_LIMIT_AMOUNT(1, "012","Merchant over limit amount"),
    INVALID_REQUEST(1, "013","Request cannot be processed"),
    INVALID_COUNTRY(1, "014","Unsupported Country"),
    ACCT_NOT_FOUND(1, "015","Account not found"),
    INVALID_PARTICIPANT_CODE(1, "016","Invalid participant code"),
    QR_EXPIRED(1, "017","QR Expired"),
    QR_NOT_FOUND(1, "018","QR not found"),
    INVALID_TXN(1, "009","Invalid Transaction"), BAD_REQUEST(1,"400" ,"Student has already register" );

    private final Integer responseCode;
    private final String errorCode;
    private final String message;

    CommonCode(Integer responseCode, String errorCode, String message) {
        this.responseCode = responseCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

}
