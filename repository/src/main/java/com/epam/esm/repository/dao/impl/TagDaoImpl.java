package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import static com.epam.esm.repository.dao.DatabaseColumn.*;
import com.epam.esm.repository.dao.SqlQueryBuilder;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.repository.exception.OperationNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class TagDaoImpl implements TagDao {

    private JdbcOperations jdbcOperations;

    @Autowired
    public TagDaoImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public int create(Tag entity) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addInsertClause(TAG_TABLE, TAG_NAME);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcOperations.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(queryBuilder.build(), Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, entity.getName());
                return statement;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new DaoException("Unable to create tag", e);
        }

        return keyHolder.getKey().intValue();
    }

    @Override
    public Optional<Tag> findById(int id) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectClause(TAG_TABLE, TAG_ID, TAG_NAME)
                .addWhereClause(TAG_ID + " = ?");

        try {
            return Optional.ofNullable(jdbcOperations.query(queryBuilder.build(), rs -> {
                if (rs.next()) {
                    return mapTag(rs, 1);
                }
                return null;
            }, id));
        } catch (DataAccessException e) {
            throw new DaoException("Unable to find tag by id (id = " + id + ")", e);
        }
    }

    @Override
    public List<Tag> findAll() throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectClause(TAG_TABLE, TAG_ID, TAG_NAME);

        try {
            return jdbcOperations.query(queryBuilder.build(), this::mapTag);
        } catch (DataAccessException e) {
            throw new DaoException("Unable to find all tags", e);
        }
    }

    @Override
    public boolean deleteById(int id) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addDeleteClause(TAG_TABLE)
                .addWhereClause(TAG_ID + " = ?");

        try {
            return jdbcOperations.update(queryBuilder.build(), id) > 0;
        } catch (DataAccessException e) {
            throw new DaoException("Unable to delete tag (id = " + id + ")", e);
        }
    }

    @Override
    public Optional<Tag> update(Tag entity) {
        throw new OperationNotSupportedException("Update operation not supported for tags");
    }

    @Override
    public Optional<Tag> findByName(String name) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectClause(TAG_TABLE, TAG_ID, TAG_NAME)
                .addWhereClause(TAG_NAME + " = ?");

        try {
            return Optional.ofNullable(jdbcOperations.query(queryBuilder.build(), rs -> {
                if (rs.next()) {
                    return mapTag(rs, 1);
                }
                return null;
            }, name));
        } catch (DataAccessException e) {
            throw new DaoException("Unable to find tag by name ( " + name + ")", e);
        }
    }

    @Override
    public List<Tag> findTagsForCertificate(GiftCertificate cert) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addSelectClause(CT_MAPPING_TABLE, TAG_TABLE + "." + TAG_ID, TAG_NAME)
                .addInnerJoinClause(TAG_TABLE, TAG_TABLE + "." + TAG_ID + " = " + CT_MAPPING_TAG_ID)
                .addWhereClause(CT_MAPPING_CERTIFICATE_ID + " = ?");

        try {
            return jdbcOperations.query(queryBuilder.build(), this::mapTag, cert.getId());
        } catch (DataAccessException e) {
            throw new DaoException("Unable to find tags for certificate (id = " + cert.getId() + ")", e);
        }
    }

    private Tag mapTag(ResultSet resultSet, int rowNum) throws SQLException {
        Tag tag = new Tag(resultSet.getString(TAG_NAME));
        tag.setId(resultSet.getInt(TAG_ID));
        return tag;
    }
}
