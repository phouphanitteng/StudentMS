package kh.com.acleda.student.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthReq {

    private String email;
    private String password;

}
