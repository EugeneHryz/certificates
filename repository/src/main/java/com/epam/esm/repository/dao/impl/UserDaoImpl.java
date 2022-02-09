package com.epam.esm.repository.dao.impl;

import static com.epam.esm.repository.dao.query.DatabaseName.*;
import com.epam.esm.repository.dao.query.SqlQueryBuilder;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.DaoException;
import org.springframework.dao.DataAccessException;
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
public class UserDaoImpl implements UserDao {

    private JdbcOperations jdbcOperations;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int create(User entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findById(int id) throws DaoException {
        User user = entityManager.find(User.class, id);

        return Optional.ofNullable(user);
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

        return query.getResultList();
    }

    @Override
    // todo: !!!
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
    // todo: !!!
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(User.class)));
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    @Override
    public Optional<User> update(User entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
