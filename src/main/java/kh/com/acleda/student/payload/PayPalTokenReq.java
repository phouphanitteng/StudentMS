package kh.com.acleda.student.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PayPalTokenReq {

    @NotEmpty(message = "clientId is mandatory")
    private String clientId;

    @NotEmpty(message = "clientSecret is mandatory")
    private String clientSecret;
}
