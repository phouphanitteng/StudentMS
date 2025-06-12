package kh.com.acleda.student.service.impl;

import jakarta.transaction.Transactional;
import kh.com.acleda.student.payload.Response;
import kh.com.acleda.student.payload.StudentReq;
import kh.com.acleda.student.payload.StudentResp;
import kh.com.acleda.student.payload.StudentUpdateReq;
import kh.com.acleda.student.entity.Student;
import kh.com.acleda.student.entity.StudentPk;
import kh.com.acleda.student.repository.StudentRepository;
import kh.com.acleda.student.service.StudentService;
import kh.com.acleda.student.constant.CommonCode;
import kh.com.acleda.student.utils.CommonUtils;
import kh.com.acleda.student.constant.ConstantVariable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response<?> createStudent(StudentReq studentReq) {
        Optional<Student> studentDb = studentRepository.findByIdEmail(studentReq.getEmail());
        if(studentDb.isPresent()){
            return CommonUtils.modifyRespMsgForFail(CommonCode.BAD_REQUEST, ConstantVariable.STUDENT_EXIST);
        }
        Student studentMapper = modelMapper.map(studentReq, Student.class);
        String password = CommonUtils.hashSHA256(studentReq.getPassword(), studentReq.getEmail());
        studentMapper.setPassword(password);
        studentMapper.setStatus("ACTIVE");
        studentMapper.setId(StudentPk.builder().studentId(CommonUtils.generateStdId()).email(studentReq.getEmail()).build());
        studentMapper.setLastLogin(LocalDateTime.now());
        studentMapper.setDateRegister(LocalDate.now());
        Student student = studentRepository.save(studentMapper);
        return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.CREATE_SUCCESS, student);
    }

    @Override
    public Response<?> getAllStudents() {
        List<StudentResp> studentResponses = studentRepository.findAll()
                .stream()
                .map(student -> {
                    StudentResp resp = new StudentResp();
                    resp.setStudentId(student.getId().getStudentId());
                    resp.setEmail(student.getId().getEmail());
                    resp.setFirstName(student.getFirstName());
                    resp.setLastName(student.getLastName());
                    resp.setGender(student.getGender());
                    resp.setPhoneNumber(student.getPhoneNumber());
                    resp.setDob(student.getDob());
                    resp.setDateRegister(student.getDateRegister());
                    resp.setStatus(student.getStatus());
                    resp.setLastLogin(student.getLastLogin());
                    resp.setLastLoginIP(student.getLastLoginIP());
                    return resp;
                })
                .toList();

        return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.SUCCESS, studentResponses);
    }


    @Override
    public Response<?> getStudentById(String studentId) {
        StudentResp student = studentRepository.findByIdStudentId(studentId)
                .map(result -> modelMapper.map(result, StudentResp.class))
                .orElseThrow(()-> new RuntimeException("Student Id is not found"));
        return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.SUCCESS, student);
    }

    @Override
    public Response<?> updateStudent(StudentUpdateReq studentReq) {
        // Validate input
        if (studentReq.getStudentId() == null) {
            throw new IllegalArgumentException("Student ID is required");
        }

        // Find existing student by studentId
        Student existingStudent = studentRepository.findByIdStudentId(studentReq.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student with ID " + studentReq.getStudentId() + " not found"));

        // Update only provided fields
        if (studentReq.getFirstName() != null) {
            existingStudent.setFirstName(studentReq.getFirstName());
        }
        if (studentReq.getLastName() != null) {
            existingStudent.setLastName(studentReq.getLastName());
        }
        if (studentReq.getGender() != null) {
            existingStudent.setGender(studentReq.getGender());
        }
        if (studentReq.getPhoneNumber() != null) {
            existingStudent.setPhoneNumber(studentReq.getPhoneNumber());
        }
        if (studentReq.getDob() != null) {
            existingStudent.setDob(studentReq.getDob());
        }
        if (studentReq.getStatus() != null) {
            existingStudent.setStatus(studentReq.getStatus());
        }

        // Save updated student
        Student updatedStudent = studentRepository.save(existingStudent);

        // Map to response DTO
        StudentResp studentResp = modelMapper.map(updatedStudent, StudentResp.class);

        // Log safely
        log.debug("Updated student with ID: {}", studentResp.getStudentId());

        // Return response
        return CommonUtils.modifyRespMsgForSuccess(
                CommonCode.SUCCESS,
                ConstantVariable.UPDATE_SUCCESS, // Use UPDATE_SUCCESS instead of CREATE_SUCCESS
                studentResp
        );
    }
    @Override
    @Transactional
    public Response<?> deleteStudentById(String studentId) {
        studentRepository.deleteByIdStudentId(studentId);
        return CommonUtils.modifyRespMsgForSuccess(CommonCode.SUCCESS, ConstantVariable.STD_DELETED, null);
    }
}
