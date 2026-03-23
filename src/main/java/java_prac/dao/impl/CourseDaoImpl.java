package java_prac.dao.impl;

import java_prac.dao.CourseDao;
import java_prac.model.Course;
import java_prac.model.CourseTeacher;
import java_prac.model.CourseTeacherId;
import java_prac.model.Lesson;
import java_prac.model.Student;
import java_prac.model.Teacher;
import java_prac.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
            return course;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Course> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Course.class, id));
        }
    }

    @Override
    public List<Course> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Course order by id", Course.class).getResultList();
        }
    }

    @Override
    public Course update(Course course) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Course merged = (Course) session.merge(course);
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
            Course course = session.get(Course.class, id);
            if (course == null) {
                transaction.commit();
                return false;
            }
            session.remove(course);
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
    public List<Student> findStudentsByCourseId(Long courseId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("""
                    select sc.student
                    from StudentCourse sc
                    where sc.course.id = :courseId
                    order by sc.student.id
                    """, Student.class)
                    .setParameter("courseId", courseId)
                    .getResultList();
        }
    }

    @Override
    public List<Teacher> findTeachersByCourseId(Long courseId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("""
                    select ct.teacher
                    from CourseTeacher ct
                    where ct.course.id = :courseId
                    order by ct.teacher.id
                    """, Teacher.class)
                    .setParameter("courseId", courseId)
                    .getResultList();
        }
    }

    @Override
    public boolean addTeacherToCourse(Long courseId, Long teacherId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Course course = session.get(Course.class, courseId);
            Teacher teacher = session.get(Teacher.class, teacherId);

            if (course == null || teacher == null) {
                transaction.commit();
                return false;
            }

            CourseTeacherId id = new CourseTeacherId(courseId, teacherId);
            CourseTeacher existing = session.get(CourseTeacher.class, id);

            if (existing != null) {
                transaction.commit();
                return false;
            }

            CourseTeacher courseTeacher = CourseTeacher.builder()
                    .id(id)
                    .course(course)
                    .teacher(teacher)
                    .build();

            session.persist(courseTeacher);
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
    public Lesson addLesson(Long courseId, Long teacherId, LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !end.isAfter(start)) {
            return null;
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Course course = session.get(Course.class, courseId);
            Teacher teacher = session.get(Teacher.class, teacherId);

            if (course == null || teacher == null) {
                transaction.commit();
                return null;
            }

            Lesson lesson = Lesson.builder()
                    .course(course)
                    .teacher(teacher)
                    .startTime(start)
                    .endTime(end)
                    .build();

            session.persist(lesson);
            transaction.commit();
            return lesson;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}