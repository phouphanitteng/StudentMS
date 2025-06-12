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
    public static final int CONNECTED_TIMEOUT = 5000;
    public static final int READ_TIMEOUT = 5000;
    public static final int WRITE_TIMEOUT = 5000;
}
