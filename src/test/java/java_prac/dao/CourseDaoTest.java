package java_prac.dao;

import java_prac.dao.impl.CourseDaoImpl;
import java_prac.model.Course;
import java_prac.model.CourseDurationType;
import java_prac.model.Lesson;
import java_prac.model.Student;
import java_prac.model.Teacher;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CourseDaoTest {

    private CourseDao courseDao;

    @BeforeMethod
    public void setUp() {
        courseDao = new CourseDaoImpl();
    }

    @Test
    public void testFindAllReturnsNotEmptyList() {
        List<Course> courses = courseDao.findAll();

        Assert.assertNotNull(courses);
        Assert.assertFalse(courses.isEmpty());
        Assert.assertTrue(courses.size() >= 3);
    }

    @Test
    public void testFindByIdExisting() {
        Optional<Course> course = courseDao.findById(1L);

        Assert.assertTrue(course.isPresent());
        Assert.assertEquals(course.get().getId(), 1L);
        Assert.assertNotNull(course.get().getTitle());
    }

    @Test
    public void testFindByIdNotExisting() {
        Optional<Course> course = courseDao.findById(999999L);

        Assert.assertTrue(course.isEmpty());
    }

    @Test
    public void testSave() {
        Course course = Course.builder()
                .title("Новый тестовый курс")
                .durationType(CourseDurationType.DAY)
                .hoursPerDay(6)
                .description("Описание тестового курса")
                .build();

        Course saved = courseDao.save(course);

        Assert.assertNotNull(saved.getId());

        Optional<Course> fromDb = courseDao.findById(saved.getId());
        Assert.assertTrue(fromDb.isPresent());
        Assert.assertEquals(fromDb.get().getTitle(), "Новый тестовый курс");
    }

    @Test
    public void testUpdate() {
        Course course = Course.builder()
                .title("Старое название курса")
                .durationType(CourseDurationType.DAY)
                .hoursPerDay(4)
                .description("Старое описание")
                .build();

        Course saved = courseDao.save(course);
        saved.setTitle("Новое название курса");

        Course updated = courseDao.update(saved);

        Assert.assertEquals(updated.getTitle(), "Новое название курса");

        Optional<Course> fromDb = courseDao.findById(saved.getId());
        Assert.assertTrue(fromDb.isPresent());
        Assert.assertEquals(fromDb.get().getTitle(), "Новое название курса");
    }

    @Test
    public void testDeleteByIdExisting() {
        Course course = Course.builder()
                .title("Курс на удаление")
                .durationType(CourseDurationType.DAY)
                .hoursPerDay(3)
                .description("Удаляемый курс")
                .build();

        Course saved = courseDao.save(course);
        boolean deleted = courseDao.deleteById(saved.getId());

        Assert.assertTrue(deleted);
        Assert.assertTrue(courseDao.findById(saved.getId()).isEmpty());
    }

    @Test
    public void testDeleteByIdNotExisting() {
        boolean deleted = courseDao.deleteById(999999L);

        Assert.assertFalse(deleted);
    }

    @Test
    public void testFindStudentsByCourseIdExistingCourse() {
        List<Student> students = courseDao.findStudentsByCourseId(1L);

        Assert.assertNotNull(students);
        Assert.assertFalse(students.isEmpty());
        Assert.assertTrue(students.stream().allMatch(s -> s.getId() != null));
    }

    @Test
    public void testFindStudentsByCourseIdNotExistingCourse() {
        List<Student> students = courseDao.findStudentsByCourseId(999999L);

        Assert.assertNotNull(students);
        Assert.assertTrue(students.isEmpty());
    }

    @Test
    public void testFindTeachersByCourseIdExistingCourse() {
        List<Teacher> teachers = courseDao.findTeachersByCourseId(1L);

        Assert.assertNotNull(teachers);
        Assert.assertFalse(teachers.isEmpty());
        Assert.assertTrue(teachers.stream().allMatch(t -> t.getId() != null));
    }

    @Test
    public void testFindTeachersByCourseIdNotExistingCourse() {
        List<Teacher> teachers = courseDao.findTeachersByCourseId(999999L);

        Assert.assertNotNull(teachers);
        Assert.assertTrue(teachers.isEmpty());
    }

    @Test
    public void testAddTeacherToCourseSuccess() {
        Course course = Course.builder()
                .title("Курс для назначения преподавателя")
                .durationType(CourseDurationType.DAY)
                .hoursPerDay(5)
                .description("Описание")
                .build();

        Course savedCourse = courseDao.save(course);

        boolean added = courseDao.addTeacherToCourse(savedCourse.getId(), 1L);

        Assert.assertTrue(added);

        List<Teacher> teachers = courseDao.findTeachersByCourseId(savedCourse.getId());
        Assert.assertEquals(teachers.size(), 1);
        Assert.assertEquals(teachers.get(0).getId(), 1L);
    }

    @Test
    public void testAddTeacherToCourseDuplicate() {
        Course course = Course.builder()
                .title("Курс для дубля преподавателя")
                .durationType(CourseDurationType.DAY)
                .hoursPerDay(5)
                .description("Описание")
                .build();

        Course savedCourse = courseDao.save(course);

        boolean first = courseDao.addTeacherToCourse(savedCourse.getId(), 1L);
        boolean second = courseDao.addTeacherToCourse(savedCourse.getId(), 1L);

        Assert.assertTrue(first);
        Assert.assertFalse(second);
    }

    @Test
    public void testAddTeacherToCourseCourseNotFound() {
        boolean added = courseDao.addTeacherToCourse(999999L, 1L);

        Assert.assertFalse(added);
    }

    @Test
    public void testAddTeacherToCourseTeacherNotFound() {
        Course course = Course.builder()
                .title("Курс без преподавателя")
                .durationType(CourseDurationType.DAY)
                .hoursPerDay(5)
                .description("Описание")
                .build();

        Course savedCourse = courseDao.save(course);

        boolean added = courseDao.addTeacherToCourse(savedCourse.getId(), 999999L);

        Assert.assertFalse(added);
    }

    @Test
    public void testAddLessonSuccess() {
        Lesson lesson = courseDao.addLesson(
                1L,
                1L,
                LocalDateTime.of(2026, 4, 1, 10, 0),
                LocalDateTime.of(2026, 4, 1, 12, 0)
        );

        Assert.assertNotNull(lesson);
        Assert.assertNotNull(lesson.getId());
        Assert.assertEquals(lesson.getCourse().getId(), 1L);
        Assert.assertEquals(lesson.getTeacher().getId(), 1L);
    }

    @Test
    public void testAddLessonInvalidTimeRange() {
        Lesson lesson = courseDao.addLesson(
                1L,
                1L,
                LocalDateTime.of(2026, 4, 1, 12, 0),
                LocalDateTime.of(2026, 4, 1, 10, 0)
        );

        Assert.assertNull(lesson);
    }

    @Test
    public void testAddLessonNullStart() {
        Lesson lesson = courseDao.addLesson(
                1L,
                1L,
                null,
                LocalDateTime.of(2026, 4, 1, 12, 0)
        );

        Assert.assertNull(lesson);
    }

    @Test
    public void testAddLessonNullEnd() {
        Lesson lesson = courseDao.addLesson(
                1L,
                1L,
                LocalDateTime.of(2026, 4, 1, 10, 0),
                null
        );

        Assert.assertNull(lesson);
    }

    @Test
    public void testAddLessonCourseNotFound() {
        Lesson lesson = courseDao.addLesson(
                999999L,
                1L,
                LocalDateTime.of(2026, 4, 1, 10, 0),
                LocalDateTime.of(2026, 4, 1, 12, 0)
        );

        Assert.assertNull(lesson);
    }

    @Test
    public void testAddLessonTeacherNotFound() {
        Lesson lesson = courseDao.addLesson(
                1L,
                999999L,
                LocalDateTime.of(2026, 4, 1, 10, 0),
                LocalDateTime.of(2026, 4, 1, 12, 0)
        );

        Assert.assertNull(lesson);
    }
}