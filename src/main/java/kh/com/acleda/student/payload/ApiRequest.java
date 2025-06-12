package kh.com.acleda.student.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ApiRequest<T> {

    private ApiReqHeader apiReqHeader;
    @Valid
    private T apiReqBody;

}
