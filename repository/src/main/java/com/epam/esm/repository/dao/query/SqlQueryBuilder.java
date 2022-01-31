package com.epam.esm.repository.dao.query;

import java.util.Arrays;

public class SqlQueryBuilder {

    private static final String INSERT_STATEMENT = "INSERT INTO";
    private static final String DELETE_STATEMENT = "DELETE FROM";
    private static final String VALUES_STATEMENT = "VALUES";
    private static final String SELECT_STATEMENT = "SELECT";
    private static final String WHERE_STATEMENT = "WHERE";
    private static final String UPDATE_STATEMENT = "UPDATE";
    private static final String ORDER_BY_STATEMENT = "ORDER BY";
    private static final String GROUP_BY_STATEMENT = "GROUP BY";
    private static final String INNER_JOIN_STATEMENT = "INNER JOIN";
    private static final String LIMIT_STATEMENT = "LIMIT";
    private static final String OFFSET_STATEMENT = "OFFSET";
    private static final String COUNT_FUNCTION = "COUNT";

    private StringBuilder queryBuilder;

    public SqlQueryBuilder() {
        queryBuilder = new StringBuilder();
    }

    public SqlQueryBuilder addInsertClause(String tableName, String... columnNames) {
        queryBuilder.append(INSERT_STATEMENT)
                .append(" ")
                .append(tableName)
                .append("(");

        Arrays.stream(columnNames).forEach((column) -> {
            queryBuilder.append(column);
            if (!column.equals(columnNames[columnNames.length - 1])) {
                queryBuilder.append(", ");
            } else {
                queryBuilder.append(") ");
            }
        });
        queryBuilder.append(VALUES_STATEMENT)
                .append(" (");
        for (int i = 0; i < columnNames.length; i++) {
            if (i != columnNames.length - 1) {
                queryBuilder.append("?, ");
            } else {
                queryBuilder.append("?)");
            }
        }
        return this;
    }

    public SqlQueryBuilder addSelectClause(String tableName, String... columnNames) {
        queryBuilder.append(SELECT_STATEMENT)
                .append(" ");

        Arrays.stream(columnNames).forEach((column) -> {
            queryBuilder.append(column);
            if (!column.equals(columnNames[columnNames.length - 1])) {
                queryBuilder.append(", ");
            } else {
                queryBuilder.append(" ");
            }
        });
        queryBuilder.append("FROM ")
                .append(tableName);

        return this;
    }

    public SqlQueryBuilder addOrderByClause(String columnName, String sortOrder) {
        queryBuilder.append(" ")
                .append(ORDER_BY_STATEMENT)
                .append(" ")
                .append(columnName);

        if (sortOrder.equals("desc")) {
            queryBuilder.append(" DESC");
        }
        return this;
    }

    public SqlQueryBuilder addGroupByClause(String columnName) {
        queryBuilder.append(" ")
                .append(GROUP_BY_STATEMENT)
                .append(" ")
                .append(columnName);

        return this;
    }

    public SqlQueryBuilder addUpdateClause(String tableName, String... columnNames) {
        queryBuilder.append(UPDATE_STATEMENT)
                .append(" ")
                .append(tableName)
                .append(" SET ");

        Arrays.stream(columnNames).forEach((column) -> {
            queryBuilder.append(column);
            if (!column.equals(columnNames[columnNames.length - 1])) {
                queryBuilder.append(" = ?, ");
            } else {
                queryBuilder.append(" = ?");
            }
        });
        return this;
    }

    public SqlQueryBuilder addInnerJoinClause(String tableName, String condition) {
        queryBuilder.append(" ")
                .append(INNER_JOIN_STATEMENT)
                .append(" ")
                .append(tableName)
                .append(" ON ")
                .append(condition);

        return this;
    }

    public SqlQueryBuilder addDeleteClause(String tableName) {
        queryBuilder.append(DELETE_STATEMENT)
                .append(" ")
                .append(tableName);

        return this;
    }

    public SqlQueryBuilder addWhereClause(String conditions) {
        queryBuilder.append(" ")
                .append(WHERE_STATEMENT)
                .append(" ")
                .append(conditions);

        return this;
    }

    public SqlQueryBuilder addLimitAndOffset() {
        queryBuilder.append(" ")
                .append(LIMIT_STATEMENT)
                .append(" ? ")
                .append(OFFSET_STATEMENT)
                .append(" ?");

        return this;
    }

    public String build() {
        return queryBuilder.toString();
    }
}
