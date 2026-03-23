package java_prac.dao;

import java_prac.model.Lesson;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LessonDao {
    Lesson save(Lesson lesson);
    Optional<Lesson> findById(Long id);
    List<Lesson> findAll();
    Lesson update(Lesson lesson);
    boolean deleteById(Long id);

    List<Lesson> findByCourseId(Long courseId);
    List<Lesson> findByTeacherIdAndPeriod(Long teacherId, LocalDateTime from, LocalDateTime to);
    List<Lesson> findByStudentIdAndPeriod(Long studentId, LocalDateTime from, LocalDateTime to);
}