package kh.com.acleda.student.constant;

import org.springframework.stereotype.Component;

@Component
public class ConstantVariable {
    public static final String SUCCESS = "Success";
    public static final String CREATE_SUCCESS = "Creating student successfully";
    public static final String EXTERNAL_SUC = "Called External API successfully";
    public static final String TOKEN_ERR = "Invalid token" ;
    public static final String NO_PERMISSION = "No permission";
    public static final String STUDENT_EXIST = "Student has already registered";
    public static final String REGISTER_SUCCESS = "Student registered Successfully" ;
    public static final String AUTH_SUCCESS = "Authentication successfully";
    public static final String STD_DELETED = "Student has been deleted successfully";
    public static final String INVALID_HEADER = "Invalid Request Header";
    public static final String UPDATE_SUCCESS = "Student has been updated successfully";
    public static final int CONNECTED_TIMEOUT = 100;
    public static final int READ_TIMEOUT = 100;
    public static final int WRITE_TIMEOUT = 100;
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHORIZED_BASIC = "BASIC";
    public static final String AUTHORIZED_BEARER = "BEARER";
    public static final String JWT_EXPIRED = "JWT token has expired";
    public static final String JWT_INVALID = "Invalid JWT token";
    public static final String JWT_UNSUPPORT = "Unsupported JWT token";
}
