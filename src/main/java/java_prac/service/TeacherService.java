package java_prac.service;

import java_prac.model.Teacher;
import java_prac.web.form.TeacherForm;
import java_prac.web.view.TeacherDetailsView;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    List<Teacher> findAll();
    Optional<TeacherDetailsView> findDetailsById(Long id);
    Teacher create(TeacherForm form);
    Optional<Teacher> update(Long id, TeacherForm form);
    boolean deleteById(Long id);
}