package java_prac.dao.impl;

import java_prac.dao.CompanyDao;
import java_prac.model.Company;
import java_prac.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(company);
            transaction.commit();
            return company;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Company> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Company.class, id));
        }
    }

    @Override
    public List<Company> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Company order by id", Company.class).getResultList();
        }
    }

    @Override
    public Company update(Company company) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Company merged = (Company) session.merge(company);
            transaction.commit();
            return merged;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Company company = session.get(Company.class, id);
            if (company == null) {
                transaction.commit();
                return false;
            }
            session.remove(company);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}