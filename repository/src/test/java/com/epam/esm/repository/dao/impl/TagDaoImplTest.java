package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.config.TestConfig;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.exception.DaoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class,
        loader = AnnotationConfigContextLoader.class)
@SpringBootTest(properties = "spring.jpa.defer-datasource-initialization=true")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TagDaoImplTest {

    @Autowired
    private TagDao tagDao;

    @BeforeEach
    public void initDatabase() throws DaoException {
        Tag tag1 = new Tag("clothes shopping");
        Tag tag2 = new Tag("free tickets to concert");
        Tag tag3 = new Tag("aqua park");



        tagDao.create(tag1);
        tagDao.create(tag2);
        tagDao.create(tag3);
    }

    @Test
    public void createTagShouldBeCorrect() throws DaoException {
        Tag tag = new Tag("concert");

        int expected = 4;
        int actual = tagDao.create(tag);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findTagByIdShouldBeCorrect() throws DaoException {
        Tag expected = new Tag("aqua park");
        expected.setId(3);

        Optional<Tag> actual = tagDao.findById(3);

        boolean result = actual.isPresent() && actual.get().equals(expected);
        Assertions.assertTrue(result);
    }
//
//    @Test
//    public void deleteTagShouldBeCorrect() throws DaoException {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
//        tagDao = new TagDaoImpl(jdbcTemplate);
//
//        boolean result = tagDao.deleteById(4);
//        Assertions.assertTrue(result);
//    }
//
//    @Test
//    public void findTagsForCertificateShouldBeCorrect() throws DaoException {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
//        tagDao = new TagDaoImpl(jdbcTemplate);
//
//        Tag expected = new Tag("restaurant");
//        expected.setId(1);
//        GiftCertificate certificate = mock(GiftCertificate.class);
//        when(certificate.getId()).thenReturn(3);
//
//        Tag actual = tagDao.findTagsForCertificate(certificate).get(0);
//        Assertions.assertEquals(expected, actual);
//    }
}
