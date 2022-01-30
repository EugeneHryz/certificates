package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.searchoption.SearchParameter;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.exception.DaoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

public class GiftCertificateDaoImplTest {

    private EmbeddedDatabase embeddedDatabase;

    private GiftCertificateDao certificateDao;

    @BeforeEach
    public void setUp() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        embeddedDatabase = builder.generateUniqueName(true)
                .addScript("script.sql")
                .build();
    }

    @AfterEach
    public void cleanUp() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void createCertificatesShouldBeCorrect() throws DaoException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        certificateDao = new GiftCertificateDaoImpl(jdbcTemplate);

        GiftCertificate cert = new GiftCertificate("Test name", "nice certificate", 12.67, 5,
                LocalDateTime.now(), LocalDateTime.now());

        int expected = 5;
        int actual = certificateDao.create(cert);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findCertificateShouldBeCorrect() throws DaoException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        certificateDao = new GiftCertificateDaoImpl(jdbcTemplate);

        GiftCertificate expected = new GiftCertificate("Free shopping", "good certificate", 45.0, 20,
                LocalDateTime.parse("2010-09-03T13:09:30"), LocalDateTime.parse("2010-09-03T13:09:30"));
        expected.setId(1);

        Optional<GiftCertificate> cert = certificateDao.findById(1);
        boolean actual = cert.isPresent() && cert.get().equals(expected);
        Assertions.assertTrue(actual);
    }

    @Test
    public void deleteCertificateShouldBeCorrect() throws DaoException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        certificateDao = new GiftCertificateDaoImpl(jdbcTemplate);

        boolean actual = certificateDao.deleteById(1);
        Assertions.assertTrue(actual);
    }

    @Test
    public void updateCertificateShouldBeCorrect() throws DaoException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        certificateDao = new GiftCertificateDaoImpl(jdbcTemplate);

        GiftCertificate newCertificate = new GiftCertificate("New name!!!", "best certificate ever", 50.0,
                30, LocalDateTime.parse("2018-06-01T08:46:30"), LocalDateTime.parse("2018-06-01T08:46:30"));
        newCertificate.setId(2);

        Optional<GiftCertificate> updated = certificateDao.update(newCertificate);
        boolean result = updated.isPresent() && updated.get().equals(newCertificate);
        Assertions.assertTrue(result);
    }

    @Test
    public void findCertificatesShouldBeCorrect() throws DaoException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        certificateDao = new GiftCertificateDaoImpl(jdbcTemplate);

        GiftCertificate expected = new GiftCertificate("Free ride",
                "nice gift", 12.7, 10,
                LocalDateTime.parse("2005-10-11T19:01:30"), LocalDateTime.parse("2005-10-11T19:01:30"));
        expected.setId(0);

        SearchParameter options = new SearchParameter("Free", "", "date", "asc");
        GiftCertificate actual = certificateDao.findCertificates(options).get(0);

        Assertions.assertEquals(expected, actual);
    }
}
