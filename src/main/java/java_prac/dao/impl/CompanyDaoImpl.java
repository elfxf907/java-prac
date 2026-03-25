package java_prac.dao.impl;

import java_prac.dao.CompanyDao;
import java_prac.model.Company;
import java_prac.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class CompanyDaoImpl implements CompanyDao {

    private final SessionFactory sessionFactory;

    public CompanyDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public CompanyDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Company save(Company company) {
        return sessionFactory.fromTransaction(session -> {
            session.persist(company);
            return company;
        });
    }

    @Override
    public Optional<Company> findById(Long id) {
        return sessionFactory.fromSession(session ->
                Optional.ofNullable(session.find(Company.class, id))
        );
    }

    @Override
    public List<Company> findAll() {
        return sessionFactory.fromSession(session ->
                session.createQuery("from Company order by id", Company.class).getResultList()
        );
    }

    @Override
    public Company update(Company company) {
        return sessionFactory.fromTransaction(session -> (Company) session.merge(company));
    }

    @Override
    public boolean deleteById(Long id) {
        return sessionFactory.fromTransaction(session -> {
            Company company = session.find(Company.class, id);
            if (company == null) {
                return false;
            }
            session.remove(company);
            return true;
        });
    }
}