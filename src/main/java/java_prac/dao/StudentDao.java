package java_prac.dao;

import java_prac.model.Course;
import java_prac.model.Lesson;
import java_prac.model.Student;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StudentDao {
    Student save(Student student);
    Optional<Student> findById(Long id);
    List<Student> findAll();
    Student update(Student student);
    boolean deleteById(Long id);

    List<Course> findCoursesByStudentId(Long studentId);
    boolean enrollStudentToCourse(Long studentId, Long courseId);
    List<Lesson> findScheduleByStudentIdAndPeriod(Long studentId, LocalDateTime from, LocalDateTime to);
}