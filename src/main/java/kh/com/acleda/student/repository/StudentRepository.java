package kh.com.acleda.student.repository;

import kh.com.acleda.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByIdEmail(String email);

    Optional<Student> findByIdStudentId(String studentId);

    void deleteByIdStudentId(String studentId);
}
