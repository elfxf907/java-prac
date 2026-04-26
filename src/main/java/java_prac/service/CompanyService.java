package java_prac.service;

import java_prac.model.Company;
import java_prac.web.form.CompanyForm;
import java_prac.web.view.CompanyDetailsView;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<Company> findAll();
    Optional<CompanyDetailsView> findDetailsById(Long id);
    Company create(CompanyForm form);
    Optional<Company> update(Long id, CompanyForm form);
    boolean deleteById(Long id);
}