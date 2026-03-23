package java_prac.dao;

import java_prac.dao.impl.TeacherDaoImpl;
import java_prac.model.Lesson;
import java_prac.model.Teacher;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TeacherDaoTest {

    private TeacherDao teacherDao;

    @BeforeMethod
    public void setUp() {
        teacherDao = new TeacherDaoImpl();
    }

    @Test
    public void testFindAllReturnsNotEmptyList() {
        List<Teacher> teachers = teacherDao.findAll();

        Assert.assertNotNull(teachers);
        Assert.assertFalse(teachers.isEmpty());
        Assert.assertTrue(teachers.size() >= 3);
    }

    @Test
    public void testFindByIdExisting() {
        Optional<Teacher> teacher = teacherDao.findById(1L);

        Assert.assertTrue(teacher.isPresent());
        Assert.assertEquals(teacher.get().getId(), 1L);
        Assert.assertNotNull(teacher.get().getFullName());
    }

    @Test
    public void testFindByIdNotExisting() {
        Optional<Teacher> teacher = teacherDao.findById(999999L);

        Assert.assertTrue(teacher.isEmpty());
    }

    @Test
    public void testSave() {
        Teacher teacher = Teacher.builder()
                .fullName("Тестовый преподаватель Save")
                .build();

        Teacher saved = teacherDao.save(teacher);

        Assert.assertNotNull(saved.getId());

        Optional<Teacher> fromDb = teacherDao.findById(saved.getId());
        Assert.assertTrue(fromDb.isPresent());
        Assert.assertEquals(fromDb.get().getFullName(), "Тестовый преподаватель Save");
    }

    @Test
    public void testUpdate() {
        Teacher teacher = Teacher.builder()
                .fullName("Старое имя преподавателя")
                .build();

        Teacher saved = teacherDao.save(teacher);
        saved.setFullName("Новое имя преподавателя");

        Teacher updated = teacherDao.update(saved);

        Assert.assertEquals(updated.getFullName(), "Новое имя преподавателя");

        Optional<Teacher> fromDb = teacherDao.findById(saved.getId());
        Assert.assertTrue(fromDb.isPresent());
        Assert.assertEquals(fromDb.get().getFullName(), "Новое имя преподавателя");
    }

    @Test
    public void testDeleteByIdExisting() {
        Teacher teacher = Teacher.builder()
                .fullName("Преподаватель на удаление")
                .build();

        Teacher saved = teacherDao.save(teacher);
        boolean deleted = teacherDao.deleteById(saved.getId());

        Assert.assertTrue(deleted);
        Assert.assertTrue(teacherDao.findById(saved.getId()).isEmpty());
    }

    @Test
    public void testDeleteByIdNotExisting() {
        boolean deleted = teacherDao.deleteById(999999L);

        Assert.assertFalse(deleted);
    }

    @Test
    public void testFindByCourseIdExistingCourse() {
        List<Teacher> teachers = teacherDao.findByCourseId(1L);

        Assert.assertNotNull(teachers);
        Assert.assertFalse(teachers.isEmpty());
        Assert.assertTrue(teachers.stream().allMatch(t -> t.getId() != null));
    }

    @Test
    public void testFindByCourseIdNotExistingCourse() {
        List<Teacher> teachers = teacherDao.findByCourseId(999999L);

        Assert.assertNotNull(teachers);
        Assert.assertTrue(teachers.isEmpty());
    }

    @Test
    public void testFindScheduleByTeacherIdAndPeriodWhenExists() {
        List<Lesson> lessons = teacherDao.findScheduleByTeacherIdAndPeriod(
                1L,
                LocalDateTime.of(2026, 3, 1, 0, 0),
                LocalDateTime.of(2026, 3, 10, 23, 59)
        );

        Assert.assertNotNull(lessons);
        Assert.assertFalse(lessons.isEmpty());
        Assert.assertTrue(lessons.stream().allMatch(l -> l.getId() != null));
    }

    @Test
    public void testFindScheduleByTeacherIdAndPeriodWhenEmpty() {
        List<Lesson> lessons = teacherDao.findScheduleByTeacherIdAndPeriod(
                1L,
                LocalDateTime.of(2030, 1, 1, 0, 0),
                LocalDateTime.of(2030, 1, 2, 0, 0)
        );

        Assert.assertNotNull(lessons);
        Assert.assertTrue(lessons.isEmpty());
    }
}