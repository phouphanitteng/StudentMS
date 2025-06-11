package kh.com.acleda.student.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Embeddable
public class StudentPk implements Serializable {

    @Column(name = "STUDENT_ID", length = 20)
    @NotEmpty(message = "student id must be none")
    private String studentId;

    @Column(name = "EMAIL", unique = true, length = 100)
    @NotEmpty(message = "EMAIL must not empty")
    @Email(message = "Invalid email format")
    private String email;
}
