package kh.com.acleda.student.rest;


import jakarta.validation.Valid;
import kh.com.acleda.student.dto.Response;
import kh.com.acleda.student.dto.StudentReq;
import kh.com.acleda.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentAPI {

    private final StudentService studentService;

    @PostMapping("/create")
    public Response<?> create(@RequestBody @Valid StudentReq studentReq){
        return this.studentService.createStudent(studentReq);
    }

    @GetMapping("/get_all")
    public Response<?> getAllStudents(){
        return this.studentService.getAllStudents();
    }

    @GetMapping("/get_by_id/{studentId}")
    public Response<?> getStudentById(@PathVariable @Valid String studentId){
        return this.studentService.getStudentById(studentId);
    }

    @PostMapping("/update")
    public Response<?> updateStudent(@RequestBody @Valid StudentReq studentReq){
        return this.studentService.updateStudent(studentReq);
    }

    @PostMapping("/delete/{studentId}")
    public Response<?> getAllStudents(@PathVariable @Valid StudentReq studentId){
        return this.studentService.deleteStudentById(studentId);
    }

}
