package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TagServiceImpl implements TagService {

    public static final int TAG_CODE = 2;

    private TagDao tagDao;
    private UserDao userDao;

    private ConversionService conversionService;

    @Autowired
    public TagServiceImpl(TagDao tagDao, UserDao userDao, ConversionService conversionService) {
        this.tagDao = tagDao;
        this.userDao = userDao;
        this.conversionService = conversionService;
    }

    @Override
    public TagDto createTag(TagDto tagDto) throws ServiceException {
        try {
            Optional<Tag> existingTag = tagDao.findByName(tagDto.getName());
            if (existingTag.isPresent()) {
                throw new ServiceException("Tag with name '" + tagDto.getName() + "' already exists", TAG_CODE);
            }
            int generatedId = tagDao.create(conversionService.convert(tagDto, Tag.class));

            tagDto.setId(generatedId);
            return tagDto;
        } catch (DaoException e) {
            throw new ServiceException("Unable to create new tag", e, TAG_CODE);
        }
    }

    @Override
    public TagDto getTag(int id) throws NoSuchElementException, ServiceException {
        try {
            Optional<Tag> tag = tagDao.findById(id);
            if (!tag.isPresent()) {
                throw new NoSuchElementException("Unable to get tag (id = " + id + ")", TAG_CODE);
            }
            return conversionService.convert(tag.get(), TagDto.class);
        } catch (DaoException e) {
            throw new ServiceException("Unable to get tag (id = " + id + ")", e, TAG_CODE);
        }
    }

    @Override
    public List<TagDto> getTags(int pageNumber, int pageSize) throws ServiceException {
        try {
            List<Tag> tags = tagDao.getTags(pageSize, pageSize * pageNumber);
            return tags.stream().map(t -> conversionService.convert(t, TagDto.class))
                    .collect(Collectors.toList());

        } catch (DaoException e) {
            throw new ServiceException("Unable to get tags", e, TAG_CODE);
        }
    }

    @Override
    public void deleteTag(int id) throws NoSuchElementException, ServiceException {
        try {
            if (!tagDao.deleteById(id)) {
                throw new NoSuchElementException("Unable to delete tag (id = " + id + ")", TAG_CODE);
            }
        } catch (DaoException e) {
            throw new ServiceException("Unable to delete tag (id = " + id + ")", e, TAG_CODE);
        }
    }

    @Override
    public TagDto getMostWidelyUsedTagOfUserWithHighestSpending() throws ServiceException, NoSuchElementException {
        try {
            int userWithHighestSpending = userDao.getUserIdWithHighestSpending();

            int mostWidelyUsedTagId = userDao.findMostWidelyUsedUserTagId(userWithHighestSpending);
            Optional<Tag> tag = tagDao.findById(mostWidelyUsedTagId);
            if (!tag.isPresent()) {
                throw new NoSuchElementException("Unable to find tag", TAG_CODE);
            }
            return conversionService.convert(tag.get(), TagDto.class);

        } catch (DaoException e) {
            throw new ServiceException("Unable to get most widely used tag of a user " +
                    "with highest spending", e, TAG_CODE);
        }
    }

    @Override
    public long getTagCount() throws ServiceException {
        try {
            return tagDao.getCount();
        } catch (DaoException e) {
            throw new ServiceException("Unable to count all tags", e, TAG_CODE);
        }
    }
}
