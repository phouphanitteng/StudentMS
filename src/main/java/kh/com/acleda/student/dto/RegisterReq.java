package kh.com.acleda.student.dto;


import jakarta.validation.constraints.*;
import kh.com.acleda.student.entity.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RegisterReq {

    @NotEmpty(message = "First Name is mandatory")
    private String firstName;

    @NotEmpty(message = "last Name is mandatory")
    private String lastName;

    @NotNull(message = "gender is mandatory")
    private Gender gender;

    @NotEmpty(message = "email is mandatory")
    @Email
    private String email;

    @NotEmpty(message = "password is mandatory")
    private String password;

    @NotEmpty(message = "phoneNumber is mandatory")
    private String phoneNumber;

    @Past(message = "dob is mandatory")
    @NotNull(message = "dateRegister is mandatory")
    private LocalDate dob;

    private String lastLoginIP;
}
