package kh.com.acleda.student.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ApiReqHeader {

    private String userId;
    private String deviceId;
    private String email;
    private String authorization;
    private String mobileNo;

}
