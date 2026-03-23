package java_prac.dao;

import java_prac.dao.impl.LessonDaoImpl;
import java_prac.model.Course;
import java_prac.model.Lesson;
import java_prac.model.Teacher;
import java_prac.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class LessonDaoTest {

    private LessonDao lessonDao;
    private Long lessonIdForTests;

    @BeforeMethod
    public void setUp() {
        lessonDao = new LessonDaoImpl();
        lessonIdForTests = createLessonForTest();
    }

    private Long createLessonForTest() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Course course = session.find(Course.class, 1L);
            Teacher teacher = session.find(Teacher.class, 1L);

            Assert.assertNotNull(course, "Course id=1 must exist in init.sql");
            Assert.assertNotNull(teacher, "Teacher id=1 must exist in init.sql");

            Lesson lesson = Lesson.builder()
                    .course(course)
                    .teacher(teacher)
                    .startTime(LocalDateTime.of(2026, 5, 1, 10, 0))
                    .endTime(LocalDateTime.of(2026, 5, 1, 12, 0))
                    .build();

            session.persist(lesson);
            tx.commit();
            return lesson.getId();
        }
    }

    @Test
    public void testFindAllReturnsNotEmptyList() {
        List<Lesson> lessons = lessonDao.findAll();
        Assert.assertNotNull(lessons);
        Assert.assertFalse(lessons.isEmpty());
    }

    @Test
    public void testFindByIdExisting() {
        Optional<Lesson> lesson = lessonDao.findById(lessonIdForTests);
        Assert.assertTrue(lesson.isPresent());
        Assert.assertEquals(lesson.get().getId(), lessonIdForTests);
        Assert.assertNotNull(lesson.get().getStartTime());
        Assert.assertNotNull(lesson.get().getEndTime());
    }

    @Test
    public void testFindByIdNotExisting() {
        Assert.assertTrue(lessonDao.findById(999999L).isEmpty());
    }

    @Test
    public void testUpdate() {
        Optional<Lesson> optionalLesson = lessonDao.findById(lessonIdForTests);
        Assert.assertTrue(optionalLesson.isPresent());

        Lesson lesson = optionalLesson.get();
        LocalDateTime newEnd = lesson.getEndTime().plusHours(1);

        lesson.setEndTime(newEnd);
        Lesson updated = lessonDao.update(lesson);

        Assert.assertEquals(updated.getEndTime(), newEnd);

        Optional<Lesson> fromDb = lessonDao.findById(lessonIdForTests);
        Assert.assertTrue(fromDb.isPresent());
        Assert.assertEquals(fromDb.get().getEndTime(), newEnd);
    }

    @Test
    public void testDeleteByIdExisting() {
        boolean deleted = lessonDao.deleteById(lessonIdForTests);
        Assert.assertTrue(deleted);
        Assert.assertTrue(lessonDao.findById(lessonIdForTests).isEmpty());
    }

    @Test
    public void testDeleteByIdNotExisting() {
        Assert.assertFalse(lessonDao.deleteById(999999L));
    }

    @Test
    public void testFindByCourseIdExistingCourse() {
        List<Lesson> lessons = lessonDao.findByCourseId(1L);
        Assert.assertNotNull(lessons);
        Assert.assertFalse(lessons.isEmpty());
        Assert.assertTrue(lessons.stream().allMatch(l -> l.getCourse().getId().equals(1L)));
    }

    @Test
    public void testFindByCourseIdNotExistingCourse() {
        List<Lesson> lessons = lessonDao.findByCourseId(999999L);
        Assert.assertNotNull(lessons);
        Assert.assertTrue(lessons.isEmpty());
    }

    @Test
    public void testFindByTeacherIdAndPeriodWhenExists() {
        List<Lesson> lessons = lessonDao.findByTeacherIdAndPeriod(
                1L,
                LocalDateTime.of(2026, 3, 1, 0, 0),
                LocalDateTime.of(2026, 6, 1, 23, 59)
        );

        Assert.assertNotNull(lessons);
        Assert.assertFalse(lessons.isEmpty());
        Assert.assertTrue(lessons.stream().allMatch(l -> l.getTeacher().getId().equals(1L)));
    }

    @Test
    public void testFindByTeacherIdAndPeriodWhenEmpty() {
        List<Lesson> lessons = lessonDao.findByTeacherIdAndPeriod(
                1L,
                LocalDateTime.of(2030, 1, 1, 0, 0),
                LocalDateTime.of(2030, 1, 2, 0, 0)
        );

        Assert.assertNotNull(lessons);
        Assert.assertTrue(lessons.isEmpty());
    }

    @Test
    public void testFindByStudentIdAndPeriodWhenExists() {
        List<Lesson> lessons = lessonDao.findByStudentIdAndPeriod(
                1L,
                LocalDateTime.of(2026, 3, 1, 0, 0),
                LocalDateTime.of(2026, 3, 10, 23, 59)
        );

        Assert.assertNotNull(lessons);
        Assert.assertFalse(lessons.isEmpty());
    }

    @Test
    public void testFindByStudentIdAndPeriodWhenEmpty() {
        List<Lesson> lessons = lessonDao.findByStudentIdAndPeriod(
                1L,
                LocalDateTime.of(2030, 1, 1, 0, 0),
                LocalDateTime.of(2030, 1, 2, 0, 0)
        );

        Assert.assertNotNull(lessons);
        Assert.assertTrue(lessons.isEmpty());
    }
}