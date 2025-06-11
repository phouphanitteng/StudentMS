package kh.com.acleda.student.payload;

import kh.com.acleda.student.entity.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentResp {

    private String studentId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private LocalDate dob;
    private LocalDate dateRegister;
    private String status;
    private LocalDateTime lastLogin;
    private String lastLoginIP;

}
