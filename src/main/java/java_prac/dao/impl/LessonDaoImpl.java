package java_prac.dao.impl;

import java_prac.dao.LessonDao;
import java_prac.model.Lesson;
import java_prac.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class LessonDaoImpl implements LessonDao {

    private final SessionFactory sessionFactory;

    public LessonDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public LessonDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Lesson save(Lesson lesson) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(lesson);
            transaction.commit();
            return lesson;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Lesson> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Lesson.class, id));
        }
    }

    @Override
    public List<Lesson> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Lesson order by id", Lesson.class).getResultList();
        }
    }

    @Override
    public Lesson update(Lesson lesson) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Lesson merged = (Lesson) session.merge(lesson);
            transaction.commit();
            return merged;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Lesson lesson = session.find(Lesson.class, id);
            if (lesson == null) {
                transaction.commit();
                return false;
            }
            session.remove(lesson);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public List<Lesson> findByCourseId(Long courseId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("""
                    from Lesson l
                    where l.course.id = :courseId
                    order by l.startTime
                    """, Lesson.class)
                    .setParameter("courseId", courseId)
                    .getResultList();
        }
    }

    @Override
    public List<Lesson> findByTeacherIdAndPeriod(Long teacherId, LocalDateTime from, LocalDateTime to) {
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

    @Override
    public List<Lesson> findByStudentIdAndPeriod(Long studentId, LocalDateTime from, LocalDateTime to) {
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