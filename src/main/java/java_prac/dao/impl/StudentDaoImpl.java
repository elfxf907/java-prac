package java_prac.dao.impl;

import java_prac.dao.StudentDao;
import java_prac.model.Course;
import java_prac.model.CourseDurationType;
import java_prac.model.Lesson;
import java_prac.model.Student;
import java_prac.model.StudentCourse;
import java_prac.model.StudentCourseId;
import java_prac.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
            return student;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Student.class, id));
        }
    }

    @Override
    public List<Student> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Student order by id", Student.class).getResultList();
        }
    }

    @Override
    public Student update(Student student) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Student merged = (Student) session.merge(student);
            transaction.commit();
            return merged;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student == null) {
                transaction.commit();
                return false;
            }
            session.remove(student);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Course> findCoursesByStudentId(Long studentId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("""
                    select sc.course
                    from StudentCourse sc
                    where sc.student.id = :studentId
                    order by sc.enrolledAt
                    """, Course.class)
                    .setParameter("studentId", studentId)
                    .getResultList();
        }
    }

    @Override
    public boolean enrollStudentToCourse(Long studentId, Long courseId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student == null || course == null) {
                transaction.commit();
                return false;
            }

            StudentCourseId id = new StudentCourseId(studentId, courseId);
            StudentCourse existing = session.get(StudentCourse.class, id);

            if (existing != null) {
                transaction.commit();
                return false;
            }

            StudentCourse studentCourse = StudentCourse.builder()
                    .id(id)
                    .student(student)
                    .course(course)
                    .enrolledAt(LocalDateTime.now())
                    .build();

            session.persist(studentCourse);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Lesson> findScheduleByStudentIdAndPeriod(Long studentId, LocalDateTime from, LocalDateTime to) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("""
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
                    .getResultList();
        }
    }
}