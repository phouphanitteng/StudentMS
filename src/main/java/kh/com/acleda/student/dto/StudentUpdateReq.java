package kh.com.acleda.student.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import kh.com.acleda.student.entity.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StudentUpdateReq {

    @NotEmpty(message = "Student Id is required")
    private String studentId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private LocalDate dob;
    private LocalDate dateRegister;
    private String status;
    private LocalDate lastLogin;
}
