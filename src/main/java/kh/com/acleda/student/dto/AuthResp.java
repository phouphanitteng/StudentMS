package kh.com.acleda.student.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResp {
    private String accessToken;
}
