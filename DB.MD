## School Management System


## Students Table:

- StudentID (Primary Key) Must Be
- StudentFirstName
- StudentLastName
- StudentDateOfBirth
- StudentGender
- StudentAddress
- StudentPhoneNumber
- StudentEmail
- DateOfBirth
- ParentID (Foreign Key referencing Parent.ParentID)
- ClassroomID (Foreign Key referencing Classrooms.ClassroomID)

## Teachers Table:

- TeacherID (Primary Key)
- TeacherFirstName
- TeacherLastName
- TeacherDateOfBirth
- TeacherGender
- TeacherAddress
- TeacherPhoneNumber
- TeacherEmail
- DepartmentID (Foreign Key referencing Departments.DepartmentID)
- TeacherTypeID (Foreign Key referencing TeacherType.TeacherTypeID)


## Courses Table:

- CourseID (Primary Key)
- CourseName
- Description
- TeacherID (Foreign Key referencing Teacher.TeacherID)
- DepartmentID (Foreign Key referencing Departments.DepartmentID)
- StudentClassroom Table:

- TeacherID (Foreign Key referencing Teachers Table)
- StudentClassroomID (Primary Key)
- StudentID (Foreign Key referencing Students.StudentID)
- ClassroomID (Foreign Key referencing Classrooms.ClassroomID)
- EnrollmentDate

## Enrollments Table:

- EnrollmentID (Primary Key)
- StudentID (Foreign Key referencing Students.StudentID)
- CourseID (Foreign Key referencing Courses.CourseID)
- EnrollmentDate
- Grades Table:

- GradeID (Primary Key)
- GradeName

## Classrooms Table

- ClassroomID (Primary Key)
- ClassroomName
- GradeID (Foreign Key referencing Grades.GradeID)
- TeacherID (Foreign Key referencing Teacher.TeacherID)

## Parents Table

- ParentID (Primary Key)
- FirstName
- LastName
- PhoneNumber
- Email 

## StudentClassroom Table

- StudentClassroomID (Primary Key)
- StudentID (Foreign Key referencing Students.StudentID)
- ClassroomID (Foreign Key referencing Classrooms.ClassroomID)
- EnrollmentDate

## Attendance Table

- AttendanceID (Primary Key)
- StudentID (Foreign Key referencing Students.StudentID)
- ClassroomID (Foreign Key referencing Classrooms.ClassroomID)
- Date
- Status (e.g., Present, Absent)

## Exam Table

- ExamID (Primary Key)
- ExamName
- CourseID (Foreign Key referencing Courses.CourseID)
- ExamTypeID (Foreign Key referencing ExamType.ExamTypeID)
- Date

## ExamType Table

- ExamTypeID (Primary Key)
- TypeName (e.g., Midterm, Final, Quiz)

## ExamResult Table

- ExamResultID (Primary Key)
- StudentID (Foreign Key referencing Students.StudentID)
- ExamID (Foreign Key referencing Exam.ExamID)
- Score 

## TeacherDepartment Table

- DepartmentID (Primary Key)
- DepartmentName

## TeacherType Table

- TeacherTypeID (Primary Key)
- TypeName (e.g., Full-time, Part-time)

## Departments Table

- DepartmentID (Primary Key)
- DepartmentName

## Assignments Table

- AssignmentID (Primary Key)
- AssignmentName
- CourseID (Foreign Key referencing Courses.CourseID)
- DueDate

## Exams Table

- ExamID (Primary Key)
- CourseID (Foreign Key referencing Courses.CourseID)
- ExamTypeID (Foreign Key referencing ExamType.ExamTypeID)
- Date

## ExamScores Table

- ExamScoreID (Primary Key)
- ExamID (Foreign Key referencing Exam.ExamID)
- StudentID (Foreign Key referencing Students.StudentID)
- Score

## Library Table

- LibraryID (Primary Key)
- BookTitle
- Author
- ISBN
- Quantity

## BorrowedBooks Table

- BorrowID (Primary Key)
- StudentID (Foreign Key referencing Students.StudentID)
- LibraryID (Foreign Key referencing Library.LibraryID)
- BorrowDate
- ReturnDate
- 
## Fees Table

- FeeID (Primary Key)
- StudentID (Foreign Key referencing Students.StudentID)
- Amount
- DueDate
- PaidDate



# Relationships
- Classrooms and Grades: ClassroomID references GradeID.
- Classrooms and Teachers: Each classroom is associated with a teacher using TeacherID.
- Students and Parents: Each student has a parent associated using ParentID.
- StudentClassroom: Relates students and classrooms (many-to-many), creating a record for each student enrolled in a specific classroom.
- Attendance: Tracks the attendance of students for each classroom.
- Courses and Teachers: Each course is taught by a teacher, linked through TeacherID.
- Courses and Departments: Each course belongs to a department.
- Exams: Each exam is associated with a course and an exam type.
- Exam Results: Stores student scores for each exam.
- Assignments: Linked to courses.
- Library: Tracks books and their availability in the library.
- BorrowedBooks: Tracks which books are borrowed by students.
- Fees: Manages student fees and payment status.

## Reference
- [Database design](https://www.techprofree.com/school-management-system-project-database-design/)