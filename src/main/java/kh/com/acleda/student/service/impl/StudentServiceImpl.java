package kh.com.acleda.student.service.impl;

import kh.com.acleda.student.dto.Response;
import kh.com.acleda.student.dto.StudentReq;
import kh.com.acleda.student.dto.StudentResp;
import kh.com.acleda.student.entity.Student;
import kh.com.acleda.student.repository.StudentRepository;
import kh.com.acleda.student.service.StudentService;
import kh.com.acleda.student.utils.CommonCode;
import kh.com.acleda.student.utils.CommonUtils;
import kh.com.acleda.student.utils.ConstantVariable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response<?> createStudent(StudentReq studentReq) {
        Student studentMapper = modelMapper.map(studentReq, Student.class);
        Student student = studentRepository.save(studentMapper);
        return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.CREATE_SUCCESS, student);
    }

    @Override
    public Response<?> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.SUCCESS, students);
    }

    @Override
    public Response<?> getStudentById(String studentId) {
        StudentResp student = studentRepository.findById(studentId)
                .map(result -> modelMapper.map(result, StudentResp.class))
                .orElseThrow(()-> new RuntimeException("Student Id is not found"));
        return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.SUCCESS, student);
    }

    @Override
    public Response<?> updateStudent(StudentReq studentReq) {
        return null;
    }

    @Override
    public Response<?> deleteStudentById(StudentReq studentId) {
        return null;
    }
}
