package kh.com.acleda.student.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserInfoRequest {

    @NotEmpty(message = "userId is mandatory")
    private String userId;

}
