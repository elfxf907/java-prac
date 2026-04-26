package java_prac.web.view;

import java_prac.model.Course;
import java_prac.model.Lesson;
import java_prac.model.Student;
import java_prac.model.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CourseDetailsView {
    private Course course;
    private List<Teacher> teachers;
    private List<Student> students;
    private List<Lesson> lessons;
}