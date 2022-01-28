package com.epam.esm.repository.dao.impl;

import static com.epam.esm.repository.dao.query.DatabaseColumn.*;
import com.epam.esm.repository.dao.query.SqlQueryBuilder;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private JdbcOperations jdbcOperations;

    @Autowired
    public UserDaoImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public int create(User entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findById(int id) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectClause(USER_TABLE, USER_ID, USER_NAME)
                .addWhereClause(USER_ID + " = ?");

        try {
            return Optional.ofNullable(jdbcOperations.query(queryBuilder.build(), rs -> {
                if (rs.next()) {
                    return mapUser(rs, 1);
                }
                return null;
            }, id));
        } catch (DataAccessException e) {
            throw new DaoException("Unable to find user (id = " + id + ")", e);
        }
    }

    @Override
    public boolean deleteById(int id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getUsers(int limit, int offset) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectClause(USER_TABLE, USER_ID, USER_NAME)
                .addLimitAndOffset();

        try {
            return jdbcOperations.query(queryBuilder.build(), this::mapUser, limit, offset);
        } catch (DataAccessException e) {
            throw new DaoException("Unable to get users", e);
        }
    }

    @Override
    public Optional<User> update(User entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getCount() throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectCount(USER_TABLE);

        try {
            Long count = jdbcOperations.queryForObject(queryBuilder.build(), (rs, rowNum) -> rs.getLong(1));
            return count != null ? count : -1L;
        } catch (DataAccessException e) {
            throw new DaoException("Unable to count users", e);
        }
    }

    private User mapUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(rs.getString(USER_NAME));
        user.setId(rs.getInt(USER_ID));
        return user;
    }
}
