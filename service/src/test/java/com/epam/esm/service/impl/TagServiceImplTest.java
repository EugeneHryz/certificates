//package com.epam.esm.service.impl;
//
//import com.epam.esm.repository.dao.TagDao;
//import com.epam.esm.repository.entity.Tag;
//import com.epam.esm.repository.exception.DaoException;
//import com.epam.esm.service.TagService;
//import com.epam.esm.service.dto.TagDto;
//import com.epam.esm.service.exception.impl.NoSuchElementException;
//import com.epam.esm.service.exception.impl.ServiceException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.times;
//
//public class TagServiceImplTest {
//
//    @Mock
//    private TagDao tagDao;
//
//    @BeforeEach
//    public void setupMocks() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void getTagShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
//        Tag tag = new Tag("beach trip");
//        tag.setId(20);
//
//        Mockito.when(tagDao.findById(anyInt())).thenReturn(Optional.of(tag));
//        TagService tagService = new TagServiceImpl(tagDao);
//
//        TagDto expected = new TagDto(tag);
//        TagDto actual = tagService.getTag(20);
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void getTagShouldThrowException() throws DaoException {
//        Mockito.when(tagDao.findById(anyInt())).thenReturn(Optional.empty());
//        TagService tagService = new TagServiceImpl(tagDao);
//
//        Assertions.assertThrows(NoSuchElementException.class, () -> tagService.getTag(15));
//    }
//
//    @Test
//    public void getAllTagsShouldBeCorrect() throws DaoException, ServiceException {
//        Tag tag1 = new Tag("vacation");
//        tag1.setId(21);
//        Tag tag2 = new Tag("next purchase is free");
//        tag2.setId(22);
//
//        List<Tag> tags = new ArrayList<>();
//        tags.add(tag1);
//        tags.add(tag2);
//
//        Mockito.when(tagDao.findAll()).thenReturn(tags);
//        TagService tagService = new TagServiceImpl(tagDao);
//
//        List<TagDto> expected = tags.stream().map(TagDto::new).collect(Collectors.toList());
//        List<TagDto> actual = tagService.getAllTags();
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void createTagShouldBeCorrect() throws DaoException, ServiceException {
//        Mockito.when(tagDao.create(any())).thenReturn(23);
//        TagService tagService = new TagServiceImpl(tagDao);
//
//        TagDto expected = new TagDto(23, "holidays");
//        TagDto actual = tagService.createTag(new TagDto(0, "holidays"));
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void deleteTagShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
//        Mockito.when(tagDao.deleteById(anyInt())).thenReturn(true);
//        TagService tagService = new TagServiceImpl(tagDao);
//
//        tagService.deleteTag(4);
//        Mockito.verify(tagDao, times(1)).deleteById(4);
//    }
//
//    @Test
//    public void deleteTagShouldThrowException() throws DaoException {
//        Mockito.when(tagDao.deleteById(anyInt())).thenReturn(false);
//        TagService tagService = new TagServiceImpl(tagDao);
//
//        Assertions.assertThrows(NoSuchElementException.class, () -> tagService.deleteTag(10));
//    }
//}
