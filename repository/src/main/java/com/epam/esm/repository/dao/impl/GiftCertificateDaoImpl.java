package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.searchoption.CertificateSearchParameter;
import com.epam.esm.repository.entity.GiftCertificate;
import static com.epam.esm.repository.dao.query.DatabaseName.*;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.query.SqlQueryBuilder;
import com.epam.esm.repository.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private JdbcOperations jdbcOperations;

    @Autowired
    public GiftCertificateDaoImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public int create(GiftCertificate certificate) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addInsertClause(CERTIFICATE_TABLE, CERTIFICATE_NAME, CERTIFICATE_DESCRIPTION, CERTIFICATE_PRICE,
                CERTIFICATE_DURATION, CERTIFICATE_CREATE_DATE, CERTIFICATE_LAST_UPDATE_DATE);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcOperations.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(queryBuilder.build(), Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, certificate.getName());
                statement.setString(2, certificate.getDescription());
                statement.setDouble(3, certificate.getPrice());
                statement.setInt(4, certificate.getDuration());
                statement.setTimestamp(5, Timestamp.valueOf(certificate.getCreated()));
                statement.setTimestamp(6, Timestamp.valueOf(certificate.getLastUpdated()));
                return statement;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new DaoException("Unable to create certificate", e);
        }

        return keyHolder.getKey().intValue();
    }

    @Override
    public Optional<GiftCertificate> findById(int id) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        addSelectClauseForAllColumns(queryBuilder);
        queryBuilder.addWhereClause(CERTIFICATE_ID + " = ?");

        try {
            return Optional.ofNullable(jdbcOperations
                    .query(queryBuilder.build(), rs -> {
                        if (rs.next()) {
                            return mapGiftCertificate(rs, 1);
                        }
                        return null;
                    }, id));
        } catch (DataAccessException e) {
            throw new DaoException("Unable to find certificate by id (id = " + id + ")", e);
        }
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        addSelectClauseForAllColumns(queryBuilder);
        queryBuilder.addWhereClause(CERTIFICATE_NAME + " = ?");

        try {
            return Optional.ofNullable(jdbcOperations
                    .query(queryBuilder.build(), (rs) -> {
                        if (rs.next()) {
                            return mapGiftCertificate(rs, 1);
                        }
                        return null;
                    }, name));
        } catch (DataAccessException e) {
            throw new DaoException("Unable to find certificate by name (name = " + name + ")", e);
        }
    }

    @Override
    public List<GiftCertificate> findCertificates(CertificateSearchParameter options, int limit, int offset) throws DaoException {
        SqlQueryBuilder queryBuilder = constructGetCertificatesQueryBuilder(options, false);

        String sortColumn = options.getSortBy().equals("date") ? CERTIFICATE_LAST_UPDATE_DATE : CERTIFICATE_NAME;
        queryBuilder.addOrderByClause(sortColumn, options.getSortOrder())
                .addLimitAndOffset();
        try {
            System.out.println(queryBuilder.build());
            return jdbcOperations.query(queryBuilder.build(), this::mapGiftCertificate, limit, offset);
        } catch (DataAccessException e) {
            throw new DaoException("Unable to find certificates with specified parameters", e);
        }
    }

    @Override
    public boolean deleteById(int id) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addDeleteClause(CERTIFICATE_TABLE)
                .addWhereClause(CERTIFICATE_ID + " = ?");

        try {
            return jdbcOperations.update(queryBuilder.build(), id) > 0;
        } catch (DataAccessException e) {
            throw new DaoException("Unable to delete certificate by id (id = " + id + ")", e);
        }
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate cert) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addUpdateClause(CERTIFICATE_TABLE, CERTIFICATE_NAME, CERTIFICATE_DESCRIPTION,
                CERTIFICATE_PRICE, CERTIFICATE_DURATION, CERTIFICATE_CREATE_DATE, CERTIFICATE_LAST_UPDATE_DATE);
        queryBuilder.addWhereClause(CERTIFICATE_ID + " = ?");

        int rowsUpdated;
        try {
            rowsUpdated = jdbcOperations.update(queryBuilder.build(), cert.getName(), cert.getDescription(),
                    cert.getPrice(), cert.getDuration(), Timestamp.valueOf(cert.getCreated()),
                    Timestamp.valueOf(cert.getLastUpdated()), cert.getId());
        } catch (DataAccessException e) {
            throw new DaoException("Unable to update certificate (id = " + cert.getId() + ")", e);
        }

        Optional<GiftCertificate> updated = Optional.empty();
        if (rowsUpdated > 0) {
            updated = findById(cert.getId());
        }
        return updated;
    }

    @Override
    public boolean createCertificateTagMapping(int certId, int tagId) throws DaoException {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.addInsertClause(CT_MAPPING_TABLE, CT_MAPPING_CERTIFICATE_ID, CT_MAPPING_TAG_ID);

        try {
            return jdbcOperations.update(queryBuilder.build(), certId, tagId) > 0;
        } catch (DataAccessException e) {
            throw new DaoException("Unable to create certificate-tag mapping (certId = " + certId + ", " +
                    "tagId = " + tagId + ")", e);
        }
    }

    @Override
    public long getCount(CertificateSearchParameter options) throws DaoException {
        SqlQueryBuilder queryBuilder = constructGetCertificatesQueryBuilder(options, true);

        try {
            System.out.println(queryBuilder.build());
            Long count = jdbcOperations.queryForObject(queryBuilder.build(), (rs, rowNum) -> rs.getLong(1));
            return count != null ? count : -1L;
        } catch (DataAccessException e) {
            throw new DaoException("Unable to count gift certificates", e);
        }
    }

    private SqlQueryBuilder constructGetCertificatesQueryBuilder(CertificateSearchParameter options, boolean count) {
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();

        if (count) {
            queryBuilder.addSelectClause(CERTIFICATE_TABLE, "COUNT(*)");
        } else {
            addSelectClauseForAllColumns(queryBuilder);
        }
        if (options.getTagNames() != null && options.getTagNames().length > 0) {
            SqlQueryBuilder firstSubqueryBuilder = new SqlQueryBuilder();
            firstSubqueryBuilder.addSelectClause(TAG_TABLE, TAG_ID);
            StringBuilder tagsCondition = new StringBuilder();
            String[] tagNames = options.getTagNames();
            for (String tagName : tagNames) {
                tagsCondition.append(TAG_NAME + " = '")
                        .append(tagName)
                        .append("'");
                if (!Objects.equals(tagNames[tagNames.length - 1], tagName)) {
                    tagsCondition.append(" OR ");
                }
            }
            firstSubqueryBuilder.addWhereClause(tagsCondition.toString());

            SqlQueryBuilder secondSubqueryBuilder = new SqlQueryBuilder();
            secondSubqueryBuilder.addSelectClause(CT_MAPPING_TABLE, CT_MAPPING_CERTIFICATE_ID)
                    .addWhereClause(CT_MAPPING_TAG_ID + " IN (" + firstSubqueryBuilder.build() + ")")
                    .addGroupByClause(CT_MAPPING_CERTIFICATE_ID)
                    .addHavingClause("COUNT(*) = " + tagNames.length);

            String secondSubqueryAlias = "mc";
            queryBuilder.addInnerJoinClause("(" + secondSubqueryBuilder.build() + ") " +
                    secondSubqueryAlias, CT_MAPPING_CERTIFICATE_ID + " = " + CERTIFICATE_ID);
        }
        queryBuilder.addWhereClause("(" + CERTIFICATE_NAME + " LIKE '%" + options.getSearchParam() + "%' OR " +
                CERTIFICATE_DESCRIPTION + " LIKE '%" + options.getSearchParam() + "%')");

        return queryBuilder;
    }

    private void addSelectClauseForAllColumns(SqlQueryBuilder queryBuilder) {
        queryBuilder.addSelectClause(CERTIFICATE_TABLE, CERTIFICATE_ID, CERTIFICATE_NAME, CERTIFICATE_DESCRIPTION,
                CERTIFICATE_PRICE, CERTIFICATE_DURATION, CERTIFICATE_CREATE_DATE, CERTIFICATE_LAST_UPDATE_DATE);
    }

    private GiftCertificate mapGiftCertificate(ResultSet resultSet, int rowNum) throws SQLException {
        GiftCertificate certificate = new GiftCertificate(resultSet.getString(CERTIFICATE_NAME),
                resultSet.getString(CERTIFICATE_DESCRIPTION),
                resultSet.getDouble(CERTIFICATE_PRICE),
                resultSet.getInt(CERTIFICATE_DURATION),
                resultSet.getTimestamp(CERTIFICATE_CREATE_DATE).toLocalDateTime(),
                resultSet.getTimestamp(CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime());
        certificate.setId(resultSet.getInt(CERTIFICATE_ID));

        return certificate;
    }
}
