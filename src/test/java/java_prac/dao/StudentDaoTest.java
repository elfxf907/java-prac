package java_prac.dao;

import java_prac.dao.impl.StudentDaoImpl;
import java_prac.model.Course;
import java_prac.model.Lesson;
import java_prac.model.Student;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class StudentDaoTest {

    private StudentDao studentDao;

    @BeforeMethod
    public void setUp() {
        studentDao = new StudentDaoImpl();
    }

    @Test
    public void testFindAllReturnsNotEmptyList() {
        List<Student> students = studentDao.findAll();

        Assert.assertNotNull(students);
        Assert.assertFalse(students.isEmpty());
        Assert.assertTrue(students.size() >= 3);
    }

    @Test
    public void testFindByIdExisting() {
        Optional<Student> student = studentDao.findById(1L);

        Assert.assertTrue(student.isPresent());
        Assert.assertEquals(student.get().getId(), 1L);
        Assert.assertNotNull(student.get().getFullName());
    }

    @Test
    public void testFindByIdNotExisting() {
        Optional<Student> student = studentDao.findById(999999L);

        Assert.assertTrue(student.isEmpty());
    }

    @Test
    public void testSave() {
        Student student = Student.builder()
                .fullName("Тестовый Студент Save")
                .build();

        Student saved = studentDao.save(student);

        Assert.assertNotNull(saved.getId());

        Optional<Student> fromDb = studentDao.findById(saved.getId());
        Assert.assertTrue(fromDb.isPresent());
        Assert.assertEquals(fromDb.get().getFullName(), "Тестовый Студент Save");
    }

    @Test
    public void testUpdate() {
        Student student = Student.builder()
                .fullName("Старое Имя")
                .build();

        Student saved = studentDao.save(student);
        saved.setFullName("Новое Имя");

        Student updated = studentDao.update(saved);

        Assert.assertEquals(updated.getFullName(), "Новое Имя");

        Optional<Student> fromDb = studentDao.findById(saved.getId());
        Assert.assertTrue(fromDb.isPresent());
        Assert.assertEquals(fromDb.get().getFullName(), "Новое Имя");
    }

    @Test
    public void testDeleteByIdExisting() {
        Student student = Student.builder()
                .fullName("Студент на удаление")
                .build();

        Student saved = studentDao.save(student);
        boolean deleted = studentDao.deleteById(saved.getId());

        Assert.assertTrue(deleted);
        Assert.assertTrue(studentDao.findById(saved.getId()).isEmpty());
    }

    @Test
    public void testDeleteByIdNotExisting() {
        boolean deleted = studentDao.deleteById(999999L);

        Assert.assertFalse(deleted);
    }

    @Test
    public void testFindCoursesByStudentIdExistingStudent() {
        List<Course> courses = studentDao.findCoursesByStudentId(1L);

        Assert.assertNotNull(courses);
        Assert.assertFalse(courses.isEmpty());
        Assert.assertTrue(courses.stream().allMatch(c -> c.getId() != null));
    }

    @Test
    public void testFindCoursesByStudentIdNotExistingStudent() {
        List<Course> courses = studentDao.findCoursesByStudentId(999999L);

        Assert.assertNotNull(courses);
        Assert.assertTrue(courses.isEmpty());
    }

    @Test
    public void testEnrollStudentToCourseSuccess() {
        Student student = studentDao.save(Student.builder()
                .fullName("Студент для записи")
                .build());

        boolean enrolled = studentDao.enrollStudentToCourse(student.getId(), 1L);

        Assert.assertTrue(enrolled);

        List<Course> courses = studentDao.findCoursesByStudentId(student.getId());
        Assert.assertEquals(courses.size(), 1);
        Assert.assertEquals(courses.get(0).getId(), 1L);
    }

    @Test
    public void testEnrollStudentToCourseDuplicate() {
        Student student = studentDao.save(Student.builder()
                .fullName("Студент для дубля")
                .build());

        boolean first = studentDao.enrollStudentToCourse(student.getId(), 1L);
        boolean second = studentDao.enrollStudentToCourse(student.getId(), 1L);

        Assert.assertTrue(first);
        Assert.assertFalse(second);
    }

    @Test
    public void testEnrollStudentToCourseStudentNotFound() {
        boolean enrolled = studentDao.enrollStudentToCourse(999999L, 1L);

        Assert.assertFalse(enrolled);
    }

    @Test
    public void testEnrollStudentToCourseCourseNotFound() {
        Student student = studentDao.save(Student.builder()
                .fullName("Студент без курса")
                .build());

        boolean enrolled = studentDao.enrollStudentToCourse(student.getId(), 999999L);

        Assert.assertFalse(enrolled);
    }

    @Test
    public void testFindScheduleByStudentIdAndPeriodWhenExists() {
        List<Lesson> lessons = studentDao.findScheduleByStudentIdAndPeriod(
                1L,
                LocalDateTime.of(2026, 3, 1, 0, 0),
                LocalDateTime.of(2026, 3, 10, 23, 59)
        );

        Assert.assertNotNull(lessons);
        Assert.assertFalse(lessons.isEmpty());
        Assert.assertTrue(lessons.stream().allMatch(l -> l.getId() != null));
    }

    @Test
    public void testFindScheduleByStudentIdAndPeriodWhenEmpty() {
        List<Lesson> lessons = studentDao.findScheduleByStudentIdAndPeriod(
                1L,
                LocalDateTime.of(2030, 1, 1, 0, 0),
                LocalDateTime.of(2030, 1, 2, 0, 0)
        );

        Assert.assertNotNull(lessons);
        Assert.assertTrue(lessons.isEmpty());
    }
}