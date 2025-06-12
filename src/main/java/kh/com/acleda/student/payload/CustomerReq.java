package kh.com.acleda.student.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CustomerReq {

    @NotEmpty(message = "customerId is mandatory")
    private String customerId;

}
