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
@Table(name = "GRADE")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Grade {

    @Id
    @Column(name = "GRADE_ID")
    private String gradeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String desc;

}
