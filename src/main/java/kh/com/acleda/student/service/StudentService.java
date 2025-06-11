package kh.com.acleda.student.service;

import kh.com.acleda.student.payload.Response;
import kh.com.acleda.student.payload.StudentReq;
import kh.com.acleda.student.payload.StudentUpdateReq;
import org.springframework.stereotype.Service;

@Service
public interface StudentService {

    Response<?> createStudent(StudentReq studentReq);

    Response<?> getAllStudents();

    Response<?> getStudentById(String studentId);

    Response<?> updateStudent(StudentUpdateReq studentReq);

    Response<?> deleteStudentById(String studentId);
}
