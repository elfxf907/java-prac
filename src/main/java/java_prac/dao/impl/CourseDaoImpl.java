package java_prac.dao.impl;

import java_prac.dao.CourseDao;
import java_prac.model.Course;
import java_prac.model.CourseTeacher;
import java_prac.model.CourseTeacherId;
import java_prac.model.Lesson;
import java_prac.model.Student;
import java_prac.model.Teacher;
import java_prac.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CourseDaoImpl implements CourseDao {

    private final SessionFactory sessionFactory;

    public CourseDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public CourseDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Course save(Course course) {
        return sessionFactory.fromTransaction(session -> {
            session.persist(course);
            return course;
        });
    }

    @Override
    public Optional<Course> findById(Long id) {
        return sessionFactory.fromSession(session ->
                Optional.ofNullable(session.find(Course.class, id))
        );
    }

    @Override
    public List<Course> findAll() {
        return sessionFactory.fromSession(session ->
                session.createQuery("from Course order by id", Course.class).getResultList()
        );
    }

    @Override
    public Course update(Course course) {
        return sessionFactory.fromTransaction(session -> (Course) session.merge(course));
    }

    @Override
    public boolean deleteById(Long id) {
        return sessionFactory.fromTransaction(session -> {
            Course course = session.find(Course.class, id);
            if (course == null) {
                return false;
            }
            session.remove(course);
            return true;
        });
    }

    @Override
    public List<Student> findStudentsByCourseId(Long courseId) {
        return sessionFactory.fromSession(session ->
                session.createQuery("""
                        select sc.student
                        from StudentCourse sc
                        where sc.course.id = :courseId
                        order by sc.student.id
                        """, Student.class)
                        .setParameter("courseId", courseId)
                        .getResultList()
        );
    }

    @Override
    public List<Teacher> findTeachersByCourseId(Long courseId) {
        return sessionFactory.fromSession(session ->
                session.createQuery("""
                        select ct.teacher
                        from CourseTeacher ct
                        where ct.course.id = :courseId
                        order by ct.teacher.id
                        """, Teacher.class)
                        .setParameter("courseId", courseId)
                        .getResultList()
        );
    }

    @Override
    public boolean addTeacherToCourse(Long courseId, Long teacherId) {
        return sessionFactory.fromTransaction(session -> {
            Course course = session.find(Course.class, courseId);
            Teacher teacher = session.find(Teacher.class, teacherId);

            if (course == null || teacher == null) {
                return false;
            }

            CourseTeacherId id = new CourseTeacherId(courseId, teacherId);
            CourseTeacher existing = session.find(CourseTeacher.class, id);

            if (existing != null) {
                return false;
            }

            CourseTeacher courseTeacher = CourseTeacher.builder()
                    .id(id)
                    .course(course)
                    .teacher(teacher)
                    .build();

            session.persist(courseTeacher);
            return true;
        });
    }

    @Override
    public Lesson addLesson(Long courseId, Long teacherId, LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !end.isAfter(start)) {
            return null;
        }

        return sessionFactory.fromTransaction(session -> {
            Course course = session.find(Course.class, courseId);
            Teacher teacher = session.find(Teacher.class, teacherId);

            if (course == null || teacher == null) {
                return null;
            }

            Lesson lesson = Lesson.builder()
                    .course(course)
                    .teacher(teacher)
                    .startTime(start)
                    .endTime(end)
                    .build();

            session.persist(lesson);
            return lesson;
        });
    }
}