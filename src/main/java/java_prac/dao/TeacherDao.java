package java_prac.dao;

import java_prac.model.Course;
import java_prac.model.Lesson;
import java_prac.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TeacherDao {
    Teacher save(Teacher teacher);
    Optional<Teacher> findById(Long id);
    List<Teacher> findAll();
    Teacher update(Teacher teacher);
    boolean deleteById(Long id);

    List<Teacher> findByCourseId(Long courseId);
    List<Lesson> findScheduleByTeacherIdAndPeriod(Long teacherId, LocalDateTime from, LocalDateTime to);
}