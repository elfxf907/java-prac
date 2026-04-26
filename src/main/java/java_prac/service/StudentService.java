package java_prac.service;

import java_prac.model.Student;
import java_prac.web.form.StudentForm;
import java_prac.web.view.StudentDetailsView;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> findAll();
    Optional<StudentDetailsView> findDetailsById(Long id);
    Student create(StudentForm form);
    Optional<Student> update(Long id, StudentForm form);
    boolean deleteById(Long id);
    boolean enrollToCourse(Long studentId, Long courseId);
}