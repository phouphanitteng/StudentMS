package kh.com.acleda.student.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "STUDENT")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Student {

    @Id
    @Column(name = "STUDENT_ID", length = 20)
    @NotEmpty(message = "student id must be none")
    private String studentId;

    @Column(name = "EMAIL", unique = true, length = 100)
    @NotEmpty(message = "EMAIL must not empty")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "FIRST_NAME", length = 50)
    @NotEmpty(message = "First name must not empty")
    private String firstName;

    @Column(name = "LAST_NAME", length = 50)
    @NotEmpty(message = "Last name must not empty")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    private Gender gender;

    @Column(name = "PASSWORD")
    @NotEmpty(message = "PASSWORD must not empty")
    private String password;

    @Column(name = "PHONE_NUMBER", length = 10)
    @NotEmpty(message = "PHONE_NUMBER must not empty")
    private String phoneNumber;

    @Column(name = "DOB")
    @Past(message = "date of birt must not empty")
    private LocalDate dob;

    @Column(name = "DATE_REGISTER")
    @PastOrPresent(message = "register date must not empty")
    private LocalDate dateRegister;

    @Column(name = "STATUS",length = 10) // active / inactive
    private String status;

    @Column(name = "LAST_LOGIN")
    @PastOrPresent(message = "Last Login must be present")
    private LocalDate lastLogin;

    @Column(name = "LAST_LOGIN_IP")
    private String lastLoginIP;

}
