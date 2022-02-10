package com.epam.esm.service.impl;

import com.epam.esm.repository.config.PersistenceConfig;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.config.TestConfig;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PersistenceConfig.class, TestConfig.class})
public class TagServiceImplTest {

    @Autowired
    private ConversionService conversionService;

    @Mock
    private TagDao tagDao;
    @Mock
    private UserDao userDao;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTagShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
        Tag tag = new Tag("beach trip");
        tag.setId(20);

        Mockito.when(tagDao.findById(anyInt())).thenReturn(Optional.of(tag));
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        TagDto expected = conversionService.convert(tag, TagDto.class);
        TagDto actual = tagService.getTag(20);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getTagShouldThrowNoSuchElementException() throws DaoException {
        Mockito.when(tagDao.findById(anyInt())).thenReturn(Optional.empty());
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        Assertions.assertThrows(NoSuchElementException.class, () -> tagService.getTag(15));
    }

    @Test
    public void getTagShouldThrowServiceException() throws DaoException {
        Mockito.when(tagDao.findById(anyInt())).thenThrow(DaoException.class);
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> tagService.getTag(15));
    }

    @Test
    public void getAllTagsShouldBeCorrect() throws DaoException, ServiceException {
        Tag tag1 = new Tag("vacation");
        tag1.setId(21);
        Tag tag2 = new Tag("next purchase is free");
        tag2.setId(22);

        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);

        Mockito.when(tagDao.getTags(2, 0)).thenReturn(tags);
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        List<TagDto> expected = tags.stream().map(t -> conversionService.convert(t, TagDto.class))
                .collect(Collectors.toList());
        List<TagDto> actual = tagService.getTags(0, 2);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllTagsShouldThrowServiceException() throws DaoException {
        Mockito.when(tagDao.getTags(5, 0)).thenThrow(DaoException.class);
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> tagService.getTags(0, 5));
    }

    @Test
    public void createTagShouldBeCorrect() throws DaoException, ServiceException {
        Mockito.when(tagDao.findByName(anyString())).thenReturn(Optional.empty());
        Mockito.when(tagDao.create(any())).thenReturn(23);
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        TagDto expected = new TagDto("holidays");
        expected.setId(23);
        TagDto actual = tagService.createTag(new TagDto("holidays"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void createTagThatAlreadyExists() throws DaoException {
        Tag tag = new Tag("bike ride");
        tag.setId(13);

        Mockito.when(tagDao.findByName("bike ride")).thenReturn(Optional.of(tag));
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> tagService
                .createTag(conversionService.convert(tag, TagDto.class)));
    }

    @Test
    public void createTagShouldThrowServiceException() throws DaoException {
        Mockito.when(tagDao.findByName(anyString())).thenReturn(Optional.empty());
        Mockito.when(tagDao.create(any())).thenThrow(DaoException.class);
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> tagService
                .createTag(new TagDto("wowowo")));
    }

    @Test
    public void deleteTagShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
        Mockito.when(tagDao.deleteById(anyInt())).thenReturn(true);
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        tagService.deleteTag(4);
        Mockito.verify(tagDao, times(1)).deleteById(4);
    }

    @Test
    public void deleteTagTShouldThrowNoSuchElementException() throws DaoException {
        Mockito.when(tagDao.deleteById(anyInt())).thenReturn(false);
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        Assertions.assertThrows(NoSuchElementException.class, () -> tagService.deleteTag(10));
    }

    @Test
    public void deleteTagShouldThrowServiceException() throws DaoException {
        Mockito.when(tagDao.deleteById(anyInt())).thenThrow(DaoException.class);
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> tagService.deleteTag(10));
    }

    @Test
    public void getTagCountShouldBeCorrect() throws DaoException, ServiceException {
        Mockito.when(tagDao.getCount()).thenReturn(54L);
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        long expected = 54;
        long actual = tagService.getTagCount();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getTagCountShouldThrowException() throws DaoException {
        Mockito.when(tagDao.getCount()).thenThrow(DaoException.class);
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        Assertions.assertThrows(ServiceException.class, tagService::getTagCount);
    }

    @Test
    public void getMostWidelyUsedTagOfUserWithHighestSpendingShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
        Tag tag = new Tag("flight to Philippines for 10 days");
        tag.setId(11);

        Mockito.when(tagDao.findMostWidelyUsedTagOfUserWithHighestSpending()).thenReturn(Optional.of(tag));
        TagService tagService = new TagServiceImpl(tagDao, userDao, conversionService);

        TagDto expected = new TagDto("flight to Philippines for 10 days");
        expected.setId(11);
        TagDto actual = tagService.getMostWidelyUsedTagOfUserWithHighestSpending();
        Assertions.assertEquals(expected, actual);
    }
}
