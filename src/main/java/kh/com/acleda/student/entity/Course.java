package kh.com.acleda.student.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COURSE")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Course {

    @Id
    @Column(name = "COURSE_ID")
    private String courseId;

    @Column(name = "COURSE_NAME")
    private String courseName;

    @Column(name = "TEACHER_ID")
    private String teacherId;

    @Column(name = "DEPARTMENT_ID")
    private String departmentID ;

    @Column(name = "DESCRIPTION")
    private String desc;
}
