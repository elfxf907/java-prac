package java_prac.dao;

import java_prac.model.Course;
import java_prac.model.Lesson;
import java_prac.model.Student;
import java_prac.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CourseDao {
    Course save(Course course);
    Optional<Course> findById(Long id);
    List<Course> findAll();
    Course update(Course course);
    boolean deleteById(Long id);

    List<Student> findStudentsByCourseId(Long courseId);
    List<Teacher> findTeachersByCourseId(Long courseId);

    boolean addTeacherToCourse(Long courseId, Long teacherId);
    Lesson addLesson(Long courseId, Long teacherId, LocalDateTime start, LocalDateTime end);
}