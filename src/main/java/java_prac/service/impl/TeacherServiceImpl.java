package java_prac.service.impl;

import java_prac.dao.CompanyDao;
import java_prac.dao.CourseDao;
import java_prac.dao.TeacherDao;
import java_prac.model.Company;
import java_prac.model.Teacher;
import java_prac.service.TeacherService;
import java_prac.web.form.TeacherForm;
import java_prac.web.view.TeacherDetailsView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherDao teacherDao;
    private final CompanyDao companyDao;
    private final CourseDao courseDao;

    public TeacherServiceImpl(TeacherDao teacherDao, CompanyDao companyDao, CourseDao courseDao) {
        this.teacherDao = teacherDao;
        this.companyDao = companyDao;
        this.courseDao = courseDao;
    }

    @Override
    public List<Teacher> findAll() {
        return teacherDao.findAllWithCompany();
    }

    @Override
    public Optional<TeacherDetailsView> findDetailsById(Long id) {
        return teacherDao.findByIdWithCompany(id).map(teacher ->
                new TeacherDetailsView(
                        teacher,
                        courseDao.findByTeacherId(id)
                )
        );
    }

    @Override
    public Teacher create(TeacherForm form) {
        Company company = companyDao.findById(form.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Компания не найдена"));

        Teacher teacher = Teacher.builder()
                .fullName(form.getFullName().trim())
                .company(company)
                .build();

        return teacherDao.save(teacher);
    }

    @Override
    public Optional<Teacher> update(Long id, TeacherForm form) {
        Optional<Teacher> teacherOpt = teacherDao.findById(id);
        if (teacherOpt.isEmpty()) {
            return Optional.empty();
        }

        Company company = companyDao.findById(form.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Компания не найдена"));

        Teacher teacher = teacherOpt.get();
        teacher.setFullName(form.getFullName().trim());
        teacher.setCompany(company);

        return Optional.of(teacherDao.update(teacher));
    }

    @Override
    public boolean deleteById(Long id) {
        return teacherDao.deleteById(id);
    }
}