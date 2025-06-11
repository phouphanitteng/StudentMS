package kh.com.acleda.student.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TEACHER")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Teacher {

    @Id
    @Column(name = "TEACHER_ID")
    private String gradeId;

    @Column(name = "FIRST_NAME")
    @NotEmpty(message = "First name must not empty")
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotEmpty(message = "Last name must not empty")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    private Gender gender;

    @Column(name = "EMAIL")
    @NotEmpty(message = "EMAIL must not empty")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "PASSWORD")
    @NotEmpty(message = "PASSWORD must not empty")
    private String password;

    @Column(name = "PHONE_NUMBER")
    @NotEmpty(message = "PHONE_NUMBER must not empty")
    private String phoneNumber;

    @Column(name = "DOB")
    @NotEmpty(message = "date of birt must not empty")
    private LocalDate dob;

    @Column(name = "DATE_REGISTER")
    @NotEmpty(message = "register date must not empty")
    private LocalDate dateRegister;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "LAST_LOGIN")
    private LocalDate lastLogin;

    @Column(name = "LAST_LOGIN_IP")
    private LocalDate lastLoginIP;

}
