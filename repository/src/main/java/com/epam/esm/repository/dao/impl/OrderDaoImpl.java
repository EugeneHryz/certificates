package com.epam.esm.repository.dao.impl;

import static com.epam.esm.repository.dao.query.DatabaseName.*;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.query.SqlQueryBuilder;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {

    private JdbcOperations jdbcOperations;

    @Autowired
    public OrderDaoImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public int create(Order entity) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addInsertClause(ORDER_TABLE, ORDER_USER_ID, ORDER_CERTIFICATE_ID, ORDER_TOTAL, ORDER_PURCHASE_DATE);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcOperations.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(queryBuilder.build(), Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, entity.getUserId());
                statement.setInt(2, entity.getCertificateId());
                statement.setDouble(3, entity.getTotal());
                statement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
                return statement;
            }, keyHolder);

        } catch (DataAccessException e) {
            throw new DaoException("Unable to create order", e);
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public List<Order> getUserOrders(int userId, int limit, int offset) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectClause(ORDER_TABLE, ORDER_ID, ORDER_USER_ID, ORDER_CERTIFICATE_ID,
                ORDER_TOTAL, ORDER_PURCHASE_DATE)
                .addLimitAndOffset();

        try {
            return jdbcOperations.query(queryBuilder.build(), this::mapOrder, limit, offset);
        } catch (DataAccessException e) {
            throw new DaoException("Unable to get user orders (userId = " + userId + ")", e);
        }
    }

    @Override
    public Optional<Order> findById(int id) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectClause(ORDER_TABLE, ORDER_ID, ORDER_USER_ID, ORDER_CERTIFICATE_ID,
                        ORDER_TOTAL, ORDER_PURCHASE_DATE)
                .addWhereClause(ORDER_ID + " = ?");

        try {
            return Optional.ofNullable(jdbcOperations.query(queryBuilder.build(), rs -> {
                if (rs.next()) {
                    return mapOrder(rs, 1);
                }
                return null;
            }, id));
        } catch (DataAccessException e) {
            throw new DaoException("Unable to find order (id = " + id + ")", e);
        }
    }

    @Override
    public long getUserOrderCount(int userId) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectClause(ORDER_TABLE, "COUNT(*)")
                .addWhereClause(ORDER_USER_ID + " = ?");

        try {
            Long count = jdbcOperations.queryForObject(queryBuilder.build(),
                    (rs, rowNum) -> rs.getLong(1), userId);
            return count != null ? count : -1L;
        } catch (DataAccessException e) {
            throw new DaoException("Unable to get user order count (userId = " + userId + ")", e);
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

    private Order mapOrder(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt(ORDER_ID));
        order.setUserId(rs.getInt(ORDER_USER_ID));
        order.setCertificateId(rs.getInt(ORDER_CERTIFICATE_ID));
        order.setTotal(rs.getDouble(ORDER_TOTAL));
        order.setDate(rs.getTimestamp(ORDER_PURCHASE_DATE).toLocalDateTime());
        return order;
    }
}
