package java_prac.service.impl;

import java_prac.dao.CompanyDao;
import java_prac.dao.CourseDao;
import java_prac.dao.LessonDao;
import java_prac.model.Company;
import java_prac.model.Course;
import java_prac.service.CourseService;
import java_prac.web.form.CourseForm;
import java_prac.web.view.CourseDetailsView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;
    private final CompanyDao companyDao;
    private final LessonDao lessonDao;

    public CourseServiceImpl(CourseDao courseDao, CompanyDao companyDao, LessonDao lessonDao) {
        this.courseDao = courseDao;
        this.companyDao = companyDao;
        this.lessonDao = lessonDao;
    }

    @Override
    public List<Course> findAll() {
        return courseDao.findAllWithCompany();
    }

    @Override
    public Optional<CourseDetailsView> findDetailsById(Long id) {
        return courseDao.findByIdWithCompany(id).map(course ->
                new CourseDetailsView(
                        course,
                        courseDao.findTeachersByCourseId(id),
                        courseDao.findStudentsByCourseId(id),
                        lessonDao.findByCourseId(id)
                )
        );
    }

    @Override
    public Course create(CourseForm form) {
        Company company = companyDao.findById(form.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Компания не найдена"));

        Course course = Course.builder()
                .title(form.getTitle().trim())
                .description(normalizeDescription(form.getDescription()))
                .company(company)
                .durationType(form.getDurationType())
                .hoursPerDay(form.getHoursPerDay())
                .build();

        return courseDao.save(course);
    }

    @Override
    public Optional<Course> update(Long id, CourseForm form) {
        Optional<Course> courseOpt = courseDao.findById(id);
        if (courseOpt.isEmpty()) {
            return Optional.empty();
        }

        Company company = companyDao.findById(form.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Компания не найдена"));

        Course course = courseOpt.get();
        course.setTitle(form.getTitle().trim());
        course.setDescription(normalizeDescription(form.getDescription()));
        course.setCompany(company);
        course.setDurationType(form.getDurationType());
        course.setHoursPerDay(form.getHoursPerDay());

        return Optional.of(courseDao.update(course));
    }

    @Override
    public boolean deleteById(Long id) {
        return courseDao.deleteById(id);
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }
        String trimmed = description.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}