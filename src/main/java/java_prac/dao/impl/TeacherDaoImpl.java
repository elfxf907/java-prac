package java_prac.dao.impl;

import java_prac.dao.TeacherDao;
import java_prac.model.Lesson;
import java_prac.model.Teacher;
import java_prac.util.HibernateUtil;
import org.hibernate.SessionFactory;

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
        return sessionFactory.fromTransaction(session -> {
            session.persist(teacher);
            return teacher;
        });
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        return sessionFactory.fromSession(session ->
                Optional.ofNullable(session.find(Teacher.class, id))
        );
    }

    @Override
    public List<Teacher> findAll() {
        return sessionFactory.fromSession(session ->
                session.createQuery("from Teacher order by id", Teacher.class).getResultList()
        );
    }

    @Override
    public Teacher update(Teacher teacher) {
        return sessionFactory.fromTransaction(session -> (Teacher) session.merge(teacher));
    }

    @Override
    public boolean deleteById(Long id) {
        return sessionFactory.fromTransaction(session -> {
            Teacher teacher = session.find(Teacher.class, id);
            if (teacher == null) {
                return false;
            }
            session.remove(teacher);
            return true;
        });
    }

    @Override
    public List<Teacher> findByCourseId(Long courseId) {
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
    public List<Lesson> findScheduleByTeacherIdAndPeriod(Long teacherId, LocalDateTime from, LocalDateTime to) {
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
}