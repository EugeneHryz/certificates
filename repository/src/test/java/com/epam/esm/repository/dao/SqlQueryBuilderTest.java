//package com.epam.esm.repository.dao;
//
//import com.epam.esm.repository.dao.query.DatabaseName;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static com.epam.esm.repository.dao.query.DatabaseName.*;
//import static com.epam.esm.repository.dao.query.DatabaseName.CERTIFICATE_DURATION;
//
//public class SqlQueryBuilderTest {
//
//    private SqlQueryBuilder queryBuilder;
//
//    @BeforeEach
//    public void setUp() {
//        queryBuilder = new SqlQueryBuilder();
//    }
//
//    @Test
//    public void insertCertificateQueryShouldBeCorrect() {
//        queryBuilder.addInsertClause(CERTIFICATE_TABLE, CERTIFICATE_NAME, CERTIFICATE_DESCRIPTION,
//                CERTIFICATE_PRICE, CERTIFICATE_DURATION, CERTIFICATE_CREATED_DATE, CERTIFICATE_LAST_UPDATED_DATE);
//
//        String expected = "INSERT INTO gift_certificate (name, description, price, " +
//                "duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
//        String actual = queryBuilder.build();
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void selectAllCertificatesQueryShouldBeCorrect() {
//        queryBuilder.addSelectClause(CERTIFICATE_TABLE, CERTIFICATE_ID, CERTIFICATE_NAME, CERTIFICATE_DESCRIPTION,
//                CERTIFICATE_PRICE, CERTIFICATE_DURATION, CERTIFICATE_CREATED_DATE, CERTIFICATE_LAST_UPDATED_DATE);
//
//        String expected = "SELECT id, name, description, price, duration, create_date, " +
//                "last_update_date FROM gift_certificate";
//        String actual = queryBuilder.build();
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void selectCertificateByIdShouldBeCorrect() {
//        queryBuilder.addSelectClause(CERTIFICATE_TABLE, CERTIFICATE_ID, CERTIFICATE_NAME, CERTIFICATE_DESCRIPTION,
//                CERTIFICATE_PRICE, CERTIFICATE_DURATION, CERTIFICATE_CREATED_DATE, CERTIFICATE_LAST_UPDATED_DATE);
//        queryBuilder.addWhereClause(DatabaseName.CERTIFICATE_ID + " = ?");
//
//        String expected = "SELECT id, name, description, price, duration, create_date, " +
//                "last_update_date FROM gift_certificate WHERE id = ?";
//        String actual = queryBuilder.build();
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void selectCertificatesByPartOfNameOrDescription() {
//        String queryText = "ok";
//        queryBuilder.addSelectClause(CERTIFICATE_TABLE, CERTIFICATE_ID, CERTIFICATE_NAME, CERTIFICATE_DESCRIPTION,
//                CERTIFICATE_PRICE, CERTIFICATE_DURATION, CERTIFICATE_CREATED_DATE, CERTIFICATE_LAST_UPDATED_DATE);
//        queryBuilder.addWhereClause(CERTIFICATE_NAME + " LIKE '%" + queryText + "%' OR " +
//                CERTIFICATE_DESCRIPTION + " LIKE '%" + queryText + "%'");
//
//        String expected = "SELECT id, name, description, price, duration, create_date, " +
//                "last_update_date FROM gift_certificate WHERE name LIKE '%ok%' OR description LIKE '%ok%'";
//        String actual = queryBuilder.build();
//        Assertions.assertEquals(expected, actual);
//    }
//}
