package kh.com.acleda.student.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response<T> {

    private int responseCode;
    private String errorCode;
    private String errorMessage;

    @JsonProperty("data")
    private T data;

}
