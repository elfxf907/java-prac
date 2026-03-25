package java_prac.dao.impl;

import java_prac.dao.StudentDao;
import java_prac.model.Course;
import java_prac.model.Lesson;
import java_prac.model.Student;
import java_prac.model.StudentCourse;
import java_prac.model.StudentCourseId;
import java_prac.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class StudentDaoImpl implements StudentDao {

    private final SessionFactory sessionFactory;

    public StudentDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public StudentDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Student save(Student student) {
        return sessionFactory.fromTransaction(session -> {
            session.persist(student);
            return student;
        });
    }

    @Override
    public Optional<Student> findById(Long id) {
        return sessionFactory.fromSession(session ->
                Optional.ofNullable(session.find(Student.class, id))
        );
    }

    @Override
    public List<Student> findAll() {
        return sessionFactory.fromSession(session ->
                session.createQuery("from Student order by id", Student.class).getResultList()
        );
    }

    @Override
    public Student update(Student student) {
        return sessionFactory.fromTransaction(session -> (Student) session.merge(student));
    }

    @Override
    public boolean deleteById(Long id) {
        return sessionFactory.fromTransaction(session -> {
            Student student = session.find(Student.class, id);
            if (student == null) {
                return false;
            }
            session.remove(student);
            return true;
        });
    }

    @Override
    public List<Course> findCoursesByStudentId(Long studentId) {
        return sessionFactory.fromSession(session ->
                session.createQuery("""
                        select sc.course
                        from StudentCourse sc
                        where sc.student.id = :studentId
                        order by sc.enrolledAt
                        """, Course.class)
                        .setParameter("studentId", studentId)
                        .getResultList()
        );
    }

    @Override
    public boolean enrollStudentToCourse(Long studentId, Long courseId) {
        return sessionFactory.fromTransaction(session -> {
            Student student = session.find(Student.class, studentId);
            Course course = session.find(Course.class, courseId);

            if (student == null || course == null) {
                return false;
            }

            StudentCourseId id = new StudentCourseId(studentId, courseId);
            StudentCourse existing = session.find(StudentCourse.class, id);

            if (existing != null) {
                return false;
            }

            StudentCourse studentCourse = StudentCourse.builder()
                    .id(id)
                    .student(student)
                    .course(course)
                    .enrolledAt(LocalDateTime.now())
                    .build();

            session.persist(studentCourse);
            return true;
        });
    }

    @Override
    public List<Lesson> findScheduleByStudentIdAndPeriod(Long studentId, LocalDateTime from, LocalDateTime to) {
        return sessionFactory.fromSession(session ->
                session.createQuery("""
                        select ls.lesson
                        from LessonStudent ls
                        where ls.student.id = :studentId
                          and ls.lesson.startTime >= :from
                          and ls.lesson.endTime <= :to
                        order by ls.lesson.startTime
                        """, Lesson.class)
                        .setParameter("studentId", studentId)
                        .setParameter("from", from)
                        .setParameter("to", to)
                        .getResultList()
        );
    }
}