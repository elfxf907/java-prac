package java_prac.dao;

import java_prac.dao.impl.CompanyDaoImpl;
import java_prac.dao.impl.CourseDaoImpl;
import java_prac.dao.impl.LessonDaoImpl;
import java_prac.dao.impl.StudentDaoImpl;
import java_prac.dao.impl.TeacherDaoImpl;
import java_prac.model.Course;
import java_prac.model.Lesson;
import java_prac.model.Teacher;
import java_prac.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

public class DaoImplCoverageTest {

    @Test
    public void testCoverDaoImplConstructorsAndLessonSave() {
        SessionFactory sf = HibernateUtil.getSessionFactory();

        // Покрываем конструкторы DaoImpl(SessionFactory) + по одному простому вызову
        Assert.assertFalse(new CompanyDaoImpl(sf).findAll().isEmpty());
        Assert.assertFalse(new TeacherDaoImpl(sf).findAll().isEmpty());
        Assert.assertFalse(new StudentDaoImpl(sf).findAll().isEmpty());
        Assert.assertFalse(new CourseDaoImpl(sf).findAll().isEmpty());

        // Для LessonDaoImpl дополнительно покрываем save()
        LessonDaoImpl lessonDao = new LessonDaoImpl(sf);

        Course course = sf.fromSession(s -> s.find(Course.class, 1L));
        Teacher teacher = sf.fromSession(s -> s.find(Teacher.class, 1L));

        Assert.assertNotNull(course, "Course id=1 must exist");
        Assert.assertNotNull(teacher, "Teacher id=1 must exist");

        Lesson lesson = Lesson.builder()
                .course(course)
                .teacher(teacher)
                .startTime(LocalDateTime.of(2026, 6, 1, 10, 0))
                .endTime(LocalDateTime.of(2026, 6, 1, 12, 0))
                .build();

        Lesson saved = lessonDao.save(lesson);
        Assert.assertNotNull(saved.getId());

        Assert.assertTrue(lessonDao.deleteById(saved.getId()));
    }
}