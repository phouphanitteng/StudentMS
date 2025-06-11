package kh.com.acleda.student.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kh.com.acleda.student.entity.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StudentReq {

    private String studentId;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate dob;
    private LocalDate dateRegister;
    private String status;
    private LocalDate lastLogin;
    private String lastLoginIP;
}
