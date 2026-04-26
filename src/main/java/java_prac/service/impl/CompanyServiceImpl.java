package java_prac.service.impl;

import java_prac.dao.CompanyDao;
import java_prac.dao.CourseDao;
import java_prac.dao.TeacherDao;
import java_prac.model.Company;
import java_prac.model.Course;
import java_prac.model.Teacher;
import java_prac.service.CompanyService;
import java_prac.web.form.CompanyForm;
import java_prac.web.view.CompanyDetailsView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyDao companyDao;
    private final TeacherDao teacherDao;
    private final CourseDao courseDao;

    public CompanyServiceImpl(CompanyDao companyDao, TeacherDao teacherDao, CourseDao courseDao) {
        this.companyDao = companyDao;
        this.teacherDao = teacherDao;
        this.courseDao = courseDao;
    }

    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    @Override
    public Optional<CompanyDetailsView> findDetailsById(Long id) {
        return companyDao.findById(id).map(company -> {
            List<Teacher> teachers = teacherDao.findAll().stream()
                    .filter(t -> t.getCompany() != null && id.equals(t.getCompany().getId()))
                    .toList();

            List<Course> courses = courseDao.findAll().stream()
                    .filter(c -> c.getCompany() != null && id.equals(c.getCompany().getId()))
                    .toList();

            return new CompanyDetailsView(company, teachers, courses);
        });
    }

    @Override
    public Company create(CompanyForm form) {
        Company company = Company.builder()
                .name(form.getName().trim())
                .address(normalizeAddress(form.getAddress()))
                .build();
        return companyDao.save(company);
    }

    @Override
    public Optional<Company> update(Long id, CompanyForm form) {
        return companyDao.findById(id).map(company -> {
            company.setName(form.getName().trim());
            company.setAddress(normalizeAddress(form.getAddress()));
            return companyDao.update(company);
        });
    }

    @Override
    public boolean deleteById(Long id) {
        return companyDao.deleteById(id);
    }

    private String normalizeAddress(String address) {
        if (address == null) {
            return null;
        }
        String trimmed = address.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}