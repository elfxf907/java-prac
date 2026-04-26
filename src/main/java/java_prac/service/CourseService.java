package java_prac.service;

import java_prac.model.Course;
import java_prac.web.form.CourseForm;
import java_prac.web.view.CourseDetailsView;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> findAll();
    Optional<CourseDetailsView> findDetailsById(Long id);
    Course create(CourseForm form);
    Optional<Course> update(Long id, CourseForm form);
    boolean deleteById(Long id);
}