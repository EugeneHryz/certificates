package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.Order_;
import com.epam.esm.repository.entity.User_;
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
public class OrderDaoImpl implements OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int create(Order entity) throws DaoException {
        try {
            entityManager.persist(entity);

            return entity.getId();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to create new Order", e);
        }
    }

    @Override
    public List<Order> getUserOrders(int userId, int limit, int offset) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(orderRoot).where(criteriaBuilder.equal(orderRoot.get(Order_.user).get(User_.id), userId));
        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        try {
            return query.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to get user orders", e);
        }
    }

    @Override
    public Optional<Order> findById(int id) throws DaoException {
        try {
            Order order = entityManager.find(Order.class, id);

            return Optional.ofNullable(order);
        } catch (PersistenceException e) {
            throw new DaoException("Unable to find user by id", e);
        }
    }

    @Override
    public long getUserOrderCount(int userId) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(criteriaBuilder.count(orderRoot));
        criteriaQuery.where(criteriaBuilder.equal(orderRoot.get(Order_.user).get(User_.id), userId));
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);

        try {
            return query.getSingleResult();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to count user orders", e);
        }
    }

    @Override
    public boolean deleteById(int id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Order> update(Order entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
