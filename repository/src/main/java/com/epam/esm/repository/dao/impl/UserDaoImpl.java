package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.DaoException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int create(User entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findById(int id) throws DaoException {
        try {
            User user = entityManager.find(User.class, id);

            return Optional.ofNullable(user);
        } catch (PersistenceException e) {
            throw new DaoException("Unable to find User by id = " + id, e);
        }
    }

    @Override
    public boolean deleteById(int id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getUsers(int limit, int offset) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> userRoot = criteriaQuery.from(User.class);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery.select(userRoot));
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        try {
            return query.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to get users", e);
        }
    }

    @Override
    public long getCount() throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(User.class)));
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);

        try {
            return query.getSingleResult();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to count all users", e);
        }
    }

    @Override
    public Optional<User> update(User entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
