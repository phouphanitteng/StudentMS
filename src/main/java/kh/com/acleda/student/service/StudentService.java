package kh.com.acleda.student.service;

import jakarta.validation.Valid;
import kh.com.acleda.student.dto.Response;
import kh.com.acleda.student.dto.StudentReq;
import org.springframework.stereotype.Service;

@Service
public interface StudentService {

    Response<?> createStudent(StudentReq studentReq);

    Response<?> getAllStudents();

    Response<?> getStudentById(String studentId);

    Response<?> updateStudent(StudentReq studentReq);

    Response<?> deleteStudentById(StudentReq studentId);
}
