package java_prac.service.impl;

import java_prac.dao.CourseDao;
import java_prac.dao.LessonDao;
import java_prac.dao.TeacherDao;
import java_prac.model.Course;
import java_prac.model.Lesson;
import java_prac.model.Teacher;
import java_prac.service.LessonService;
import java_prac.web.form.LessonForm;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonDao lessonDao;
    private final CourseDao courseDao;
    private final TeacherDao teacherDao;

    public LessonServiceImpl(LessonDao lessonDao, CourseDao courseDao, TeacherDao teacherDao) {
        this.lessonDao = lessonDao;
        this.courseDao = courseDao;
        this.teacherDao = teacherDao;
    }

    @Override
    public Optional<Lesson> createForCourse(Long courseId, LessonForm form) {
        if (form.getStartTime() == null || form.getEndTime() == null) {
            return Optional.empty();
        }

        if (!form.getEndTime().isAfter(form.getStartTime())) {
            return Optional.empty();
        }

        Optional<Course> courseOpt = courseDao.findById(courseId);
        if (courseOpt.isEmpty()) {
            return Optional.empty();
        }

        Optional<Teacher> teacherOpt = teacherDao.findById(form.getTeacherId());
        if (teacherOpt.isEmpty()) {
            return Optional.empty();
        }

        Lesson lesson = Lesson.builder()
                .course(courseOpt.get())
                .teacher(teacherOpt.get())
                .startTime(form.getStartTime())
                .endTime(form.getEndTime())
                .build();

        return Optional.of(lessonDao.save(lesson));
    }
}