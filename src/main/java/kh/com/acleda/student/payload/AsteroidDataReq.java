package kh.com.acleda.student.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AsteroidDataReq {

    @PastOrPresent(message = "startDate must be past or present")
    @NotNull(message = "startDate is mandatory")
    private LocalDate startDate;

    @PastOrPresent(message = "endDate must be past or present")
    @NotNull(message = "endDate is mandatory")
    private LocalDate endDate;

}
