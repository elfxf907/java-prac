package java_prac.dao;

import java_prac.dao.impl.CompanyDaoImpl;
import java_prac.model.Company;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

public class CompanyDaoTest {

    private CompanyDao companyDao;

    @BeforeMethod
    public void setUp() {
        companyDao = new CompanyDaoImpl();
    }

    @Test
    public void testFindAllReturnsNotEmptyList() {
        List<Company> companies = companyDao.findAll();

        Assert.assertNotNull(companies);
        Assert.assertFalse(companies.isEmpty());
        Assert.assertTrue(companies.size() >= 3);
    }

    @Test
    public void testFindByIdExisting() {
        Optional<Company> company = companyDao.findById(1L);

        Assert.assertTrue(company.isPresent());
        Assert.assertEquals(company.get().getId(), 1L);
        Assert.assertNotNull(company.get().getName());
    }

    @Test
    public void testFindByIdNotExisting() {
        Optional<Company> company = companyDao.findById(999999L);

        Assert.assertTrue(company.isEmpty());
    }

    @Test
    public void testSave() {
        Company company = Company.builder()
                .name("Company DAO Test Save")
                .address("Address Save")
                .build();

        Company saved = companyDao.save(company);

        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(saved.getName(), "Company DAO Test Save");
        Assert.assertEquals(saved.getAddress(), "Address Save");

        Optional<Company> fromDb = companyDao.findById(saved.getId());
        Assert.assertTrue(fromDb.isPresent());
        Assert.assertEquals(fromDb.get().getName(), "Company DAO Test Save");
    }

    @Test
    public void testUpdate() {
        Company company = Company.builder()
                .name("Company DAO Test Update")
                .address("Old Address")
                .build();

        Company saved = companyDao.save(company);
        saved.setAddress("New Address");

        Company updated = companyDao.update(saved);

        Assert.assertNotNull(updated);
        Assert.assertEquals(updated.getId(), saved.getId());
        Assert.assertEquals(updated.getAddress(), "New Address");

        Optional<Company> fromDb = companyDao.findById(saved.getId());
        Assert.assertTrue(fromDb.isPresent());
        Assert.assertEquals(fromDb.get().getAddress(), "New Address");
    }

    @Test
    public void testDeleteByIdExisting() {
        Company company = Company.builder()
                .name("Company DAO Test Delete")
                .address("Delete Address")
                .build();

        Company saved = companyDao.save(company);
        boolean deleted = companyDao.deleteById(saved.getId());

        Assert.assertTrue(deleted);

        Optional<Company> fromDb = companyDao.findById(saved.getId());
        Assert.assertTrue(fromDb.isEmpty());
    }

    @Test
    public void testDeleteByIdNotExisting() {
        boolean deleted = companyDao.deleteById(999999L);

        Assert.assertFalse(deleted);
    }
}