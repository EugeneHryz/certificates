package com.epam.esm.repository.dao.impl;

import static com.epam.esm.repository.dao.query.DatabaseName.*;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.exception.DaoException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {

    private JdbcOperations jdbcOperations;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int create(Order entity) throws DaoException {
        entityManager.persist(entity);

        return entity.getId();
    }

    @Override
    public List<Order> getUserOrders(int userId, int limit, int offset) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(orderRoot).where(criteriaBuilder.equal(orderRoot.get(ORDER_USER_ID), userId));
        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public Optional<Order> findById(int id) throws DaoException {
        Order order = entityManager.find(Order.class, id);

        return Optional.ofNullable(order);
    }

    @Override
    public long getUserOrderCount(int userId) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(criteriaBuilder.count(orderRoot));
        criteriaQuery.where(criteriaBuilder.equal(orderRoot.get(ORDER_USER_ID), userId));
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);

        return query.getSingleResult();
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
