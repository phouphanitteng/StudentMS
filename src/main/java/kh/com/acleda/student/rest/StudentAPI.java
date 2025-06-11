package kh.com.acleda.student.rest;


import jakarta.validation.Valid;
import kh.com.acleda.student.payload.Response;
import kh.com.acleda.student.payload.StudentReq;
import kh.com.acleda.student.payload.StudentUpdateReq;
import kh.com.acleda.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentAPI {

    private final StudentService studentService;

    @PostMapping("/create")
    public Response<?> create(@Valid @RequestBody StudentReq studentReq){
        return this.studentService.createStudent(studentReq);
    }

    @GetMapping("/getAll")
    public Response<?> getAllStudents(){
        return this.studentService.getAllStudents();
    }

    @GetMapping("/getStdById")
    public Response<?> getStudentById(@Valid @RequestParam String studentId){
        return this.studentService.getStudentById(studentId);
    }

    @PostMapping("/update")
    public Response<?> updateStudent(@Valid @RequestBody StudentUpdateReq studentUpdateReq){
        return this.studentService.updateStudent(studentUpdateReq);
    }

    @PostMapping("/delete")
    public Response<?> getAllStudents(@Valid @RequestParam String studentId){
        return this.studentService.deleteStudentById(studentId);
    }

}
