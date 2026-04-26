package java_prac.service.impl;

import java_prac.dao.StudentDao;
import java_prac.dao.TeacherDao;
import java_prac.model.Lesson;
import java_prac.model.Student;
import java_prac.model.Teacher;
import java_prac.service.ScheduleService;
import java_prac.web.form.ScheduleSearchForm;
import java_prac.web.view.ScheduleResultView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final StudentDao studentDao;
    private final TeacherDao teacherDao;

    public ScheduleServiceImpl(StudentDao studentDao, TeacherDao teacherDao) {
        this.studentDao = studentDao;
        this.teacherDao = teacherDao;
    }

    @Override
    public Optional<ScheduleResultView> search(ScheduleSearchForm form) {
        if (form.getFrom() == null || form.getTo() == null) {
            return Optional.empty();
        }

        if (form.getTo().isBefore(form.getFrom())) {
            return Optional.empty();
        }

        if ("student".equals(form.getSearchType())) {
            if (form.getStudentId() == null) {
                return Optional.empty();
            }

            Optional<Student> studentOpt = studentDao.findById(form.getStudentId());
            if (studentOpt.isEmpty()) {
                return Optional.empty();
            }

            List<Lesson> lessons = studentDao.findScheduleByStudentIdAndPeriod(
                    form.getStudentId(),
                    form.getFrom(),
                    form.getTo()
            );

            return Optional.of(new ScheduleResultView(
                    "student",
                    studentOpt.get().getFullName(),
                    lessons
            ));
        }

        if ("teacher".equals(form.getSearchType())) {
            if (form.getTeacherId() == null) {
                return Optional.empty();
            }

            Optional<Teacher> teacherOpt = teacherDao.findById(form.getTeacherId());
            if (teacherOpt.isEmpty()) {
                return Optional.empty();
            }

            List<Lesson> lessons = teacherDao.findScheduleByTeacherIdAndPeriod(
                    form.getTeacherId(),
                    form.getFrom(),
                    form.getTo()
            );

            return Optional.of(new ScheduleResultView(
                    "teacher",
                    teacherOpt.get().getFullName(),
                    lessons
            ));
        }

        return Optional.empty();
    }
}