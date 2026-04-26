package java_prac.web.view;

import java_prac.model.Course;
import java_prac.model.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TeacherDetailsView {
    private Teacher teacher;
    private List<Course> courses;
}