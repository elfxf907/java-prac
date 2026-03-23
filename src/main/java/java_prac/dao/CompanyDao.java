package java_prac.dao;

import java_prac.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDao {
    Company save(Company company);
    Optional<Company> findById(Long id);
    List<Company> findAll();
    Company update(Company company);
    boolean deleteById(Long id);
}