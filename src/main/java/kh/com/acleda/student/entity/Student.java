package kh.com.acleda.student.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "STUDENT")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Student {

    @EmbeddedId
    private StudentPk id;

    @Column(name = "FIRST_NAME", length = 50)
    @NotNull(message = "First name must not empty")
    private String firstName;

    @Column(name = "LAST_NAME", length = 50)
    @NotNull(message = "Last name must not empty")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    private Gender gender;

    @Column(name = "PASSWORD")
    @NotNull(message = "PASSWORD must not empty")
    private String password;

    @Column(name = "PHONE_NUMBER", length = 10)
    @NotNull(message = "PHONE_NUMBER must not empty")
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
    private LocalDateTime lastLogin;

    @Column(name = "LAST_LOGIN_IP")
    private String lastLoginIP;
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        // Assign a default role
        return List.of(new SimpleGrantedAuthority("STUDENT"));
        // If roles are stored in the database, fetch them here
        // e.g., return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

}
