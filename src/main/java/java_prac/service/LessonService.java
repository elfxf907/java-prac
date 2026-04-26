package java_prac.service;

import java_prac.model.Lesson;
import java_prac.web.form.LessonForm;

import java.util.Optional;

public interface LessonService {
    Optional<Lesson> createForCourse(Long courseId, LessonForm form);
}
