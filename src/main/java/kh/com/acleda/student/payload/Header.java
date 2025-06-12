package kh.com.acleda.student.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Header {

    private String username;
    private String password;
    private String authorizeType;
    private String apiKeyName;
    private String apiKeyValue;
    private String clientId;
    private String clientSecret;
    private String authorization;

}
