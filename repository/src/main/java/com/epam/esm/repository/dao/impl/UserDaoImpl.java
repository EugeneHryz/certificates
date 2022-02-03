package com.epam.esm.repository.dao.impl;

import static com.epam.esm.repository.dao.query.DatabaseName.*;
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
    public int getUserIdWithHighestSpending() throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        String totalSum = "SUM(" + ORDER_TOTAL + ")";
        queryBuilder.addSelectClause(ORDER_TABLE, ORDER_USER_ID, totalSum)
                .addGroupByClause(ORDER_USER_ID)
                .addOrderByClause(totalSum, "desc");

        try {
            List<Integer> usersId = jdbcOperations.query(queryBuilder.build(), (rs, rowNum) -> rs.getInt(1));
            return !usersId.isEmpty() ? usersId.get(0) : -1;
        } catch (DataAccessException e) {
            throw new DaoException("Unable to get user with highest spending", e);
        }
    }

    @Override
    public int findMostWidelyUsedUserTagId(int userId) throws DaoException {
        // first subquery
        SqlQueryBuilder userCertsSubquery = new SqlQueryBuilder();
        String CERTIFICATE_ID_ALIAS = "certId";
        userCertsSubquery.addSelectClause(ORDER_TABLE, ORDER_CERTIFICATE_ID +
                " AS " + CERTIFICATE_ID_ALIAS)
                .addWhereClause(ORDER_USER_ID + " = " + userId);

        // second subquery
        SqlQueryBuilder certificatesTagsSubquery = new SqlQueryBuilder();
        String FIRST_SUBQUERY_ALIAS = "uc";
        certificatesTagsSubquery.addSelectClause(CT_MAPPING_TABLE, CT_MAPPING_TAG_ID)
                .addInnerJoinClause("(" + userCertsSubquery.build() + ") " +
                        FIRST_SUBQUERY_ALIAS, FIRST_SUBQUERY_ALIAS + "." + CERTIFICATE_ID_ALIAS
                        + " = " + CT_MAPPING_CERTIFICATE_ID);

        // main query
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        String SECOND_SUBQUERY_ALIAS = "ct";
        String TAGS_COUNT_ALIAS = "frequency";
        queryBuilder.addSelectClause("(" + certificatesTagsSubquery.build() + ") " + SECOND_SUBQUERY_ALIAS,
                SECOND_SUBQUERY_ALIAS + "." + CT_MAPPING_TAG_ID, "COUNT(*) AS " + TAGS_COUNT_ALIAS)
                .addGroupByClause(SECOND_SUBQUERY_ALIAS + "." + CT_MAPPING_TAG_ID)
                .addOrderByClause(TAGS_COUNT_ALIAS, "desc");

        try {
            List<Integer> tagsId = jdbcOperations.query(queryBuilder.build(), (rs, rowNum) -> rs.getInt(1));
            return !tagsId.isEmpty() ? tagsId.get(0) : -1;
        } catch (DataAccessException e) {
            throw new DaoException("Unable to get most widely user tag of a user with id = " + userId, e);
        }
    }

    @Override
    public long getCount() throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectClause(USER_TABLE, "COUNT(*)");

        try {
            Long count = jdbcOperations.queryForObject(queryBuilder.build(), (rs, rowNum) -> rs.getLong(1));
            return count != null ? count : -1L;
        } catch (DataAccessException e) {
            throw new DaoException("Unable to get user count", e);
        }
    }

    @Override
    public Optional<User> update(User entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    private User mapUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(rs.getString(USER_NAME));
        user.setId(rs.getInt(USER_ID));
        return user;
    }
}
