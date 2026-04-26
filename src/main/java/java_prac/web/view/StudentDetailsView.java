package java_prac.web.view;

import java_prac.model.Course;
import java_prac.model.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StudentDetailsView {
    private Student student;
    private List<Course> courses;
    private List<Course> availableCourses;
}