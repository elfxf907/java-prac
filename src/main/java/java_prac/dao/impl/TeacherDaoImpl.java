package java_prac.dao.impl;

import java_prac.dao.TeacherDao;
import java_prac.model.Lesson;
import java_prac.model.Teacher;
import java_prac.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TeacherDaoImpl implements TeacherDao {

    private final SessionFactory sessionFactory;

    public TeacherDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public TeacherDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Teacher save(Teacher teacher) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(teacher);
            transaction.commit();
            return teacher;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Teacher.class, id));
        }
    }

    @Override
    public List<Teacher> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Teacher order by id", Teacher.class).getResultList();
        }
    }

    @Override
    public Teacher update(Teacher teacher) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Teacher merged = (Teacher) session.merge(teacher);
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
            Teacher teacher = session.get(Teacher.class, id);
            if (teacher == null) {
                transaction.commit();
                return false;
            }
            session.remove(teacher);
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
    public List<Teacher> findByCourseId(Long courseId) {
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
    public List<Lesson> findScheduleByTeacherIdAndPeriod(Long teacherId, LocalDateTime from, LocalDateTime to) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("""
                    from Lesson l
                    where l.teacher.id = :teacherId
                      and l.startTime >= :from
                      and l.endTime <= :to
                    order by l.startTime
                    """, Lesson.class)
                    .setParameter("teacherId", teacherId)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList();
        }
    }
}