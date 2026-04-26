package java_prac.service.impl;

import java_prac.dao.CourseDao;
import java_prac.dao.StudentDao;
import java_prac.model.Course;
import java_prac.model.Student;
import java_prac.service.StudentService;
import java_prac.web.form.StudentForm;
import java_prac.web.view.StudentDetailsView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;
    private final CourseDao courseDao;

    public StudentServiceImpl(StudentDao studentDao, CourseDao courseDao) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public Optional<StudentDetailsView> findDetailsById(Long id) {
        return studentDao.findById(id).map(student -> {
            List<Course> enrolledCourses = studentDao.findCoursesByStudentId(id);
            List<Course> allCourses = courseDao.findAll();

            List<Long> enrolledIds = enrolledCourses.stream()
                    .map(Course::getId)
                    .toList();

            List<Course> availableCourses = allCourses.stream()
                    .filter(course -> !enrolledIds.contains(course.getId()))
                    .toList();

            return new StudentDetailsView(student, enrolledCourses, availableCourses);
        });
    }

    @Override
    public Student create(StudentForm form) {
        Student student = Student.builder()
                .fullName(form.getFullName().trim())
                .build();
        return studentDao.save(student);
    }

    @Override
    public Optional<Student> update(Long id, StudentForm form) {
        return studentDao.findById(id).map(student -> {
            student.setFullName(form.getFullName().trim());
            return studentDao.update(student);
        });
    }

    @Override
    public boolean deleteById(Long id) {
        return studentDao.deleteById(id);
    }

    @Override
    public boolean enrollToCourse(Long studentId, Long courseId) {
        Optional<Student> studentOpt = studentDao.findById(studentId);
        if (studentOpt.isEmpty()) {
            return false;
        }

        Optional<Course> courseOpt = courseDao.findById(courseId);
        if (courseOpt.isEmpty()) {
            return false;
        }

        List<Course> enrolledCourses = studentDao.findCoursesByStudentId(studentId);
        boolean alreadyEnrolled = enrolledCourses.stream()
                .anyMatch(course -> course.getId().equals(courseId));

        if (alreadyEnrolled) {
            return false;
        }

        return studentDao.enrollToCourse(studentId, courseId);
    }
}