package java_prac.dao.impl;

import java_prac.dao.LessonDao;
import java_prac.model.Lesson;
import java_prac.util.HibernateUtil;
import org.hibernate.SessionFactory;

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
        return sessionFactory.fromTransaction(session -> {
            session.persist(lesson);
            return lesson;
        });
    }

    @Override
    public Optional<Lesson> findById(Long id) {
        return sessionFactory.fromSession(session ->
                Optional.ofNullable(session.find(Lesson.class, id))
        );
    }

    @Override
    public List<Lesson> findAll() {
        return sessionFactory.fromSession(session ->
                session.createQuery("from Lesson order by id", Lesson.class).getResultList()
        );
    }

    @Override
    public Lesson update(Lesson lesson) {
        return sessionFactory.fromTransaction(session -> (Lesson) session.merge(lesson));
    }

    @Override
    public boolean deleteById(Long id) {
        return sessionFactory.fromTransaction(session -> {
            Lesson lesson = session.find(Lesson.class, id);
            if (lesson == null) {
                return false;
            }
            session.remove(lesson);
            return true;
        });
    }

    @Override
    public List<Lesson> findByCourseId(Long courseId) {
        return sessionFactory.fromSession(session ->
                session.createQuery("""
                        from Lesson l
                        where l.course.id = :courseId
                        order by l.startTime
                        """, Lesson.class)
                        .setParameter("courseId", courseId)
                        .getResultList()
        );
    }

    @Override
    public List<Lesson> findByTeacherIdAndPeriod(Long teacherId, LocalDateTime from, LocalDateTime to) {
        return sessionFactory.fromSession(session ->
                session.createQuery("""
                        from Lesson l
                        where l.teacher.id = :teacherId
                          and l.startTime >= :from
                          and l.endTime <= :to
                        order by l.startTime
                        """, Lesson.class)
                        .setParameter("teacherId", teacherId)
                        .setParameter("from", from)
                        .setParameter("to", to)
                        .getResultList()
        );
    }

    @Override
    public List<Lesson> findByStudentIdAndPeriod(Long studentId, LocalDateTime from, LocalDateTime to) {
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