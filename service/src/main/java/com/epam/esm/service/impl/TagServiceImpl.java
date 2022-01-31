package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.impl.TagDtoMapper;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.service.validator.QueryParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TagServiceImpl implements TagService {

    public static final int TAG_CODE = 2;

    private TagDao tagDao;
    private UserDao userDao;

    private TagDtoMapper tagMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, UserDao userDao, TagDtoMapper mapper) {
        this.tagDao = tagDao;
        this.userDao = userDao;
        tagMapper = mapper;
    }

    @Override
    public TagDto createTag(TagDto tagDto) throws ServiceException {
        try {
            Optional<Tag> existingTag = tagDao.findByName(tagDto.getName());
            if (existingTag.isPresent()) {
                throw new ServiceException("Tag with name '" + tagDto.getName() + "' already exists", TAG_CODE);
            }
            int generatedId = tagDao.create(tagMapper.toEntity(tagDto));

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
            return tagMapper.toDto(tag.get());
        } catch (DaoException e) {
            throw new ServiceException("Unable to get tag (id = " + id + ")", e, TAG_CODE);
        }
    }

    @Override
    public List<TagDto> getTags(String page, String size) throws ServiceException, InvalidRequestDataException {
        QueryParamValidator validator = new QueryParamValidator();
        if (!validator.validatePositiveInteger(page) || !validator.validatePositiveInteger(size)) {
            throw new InvalidRequestDataException("Invalid pagination parameters", TAG_CODE);
        }
        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);

        try {
            List<Tag> tags = tagDao.getTags(pageSize, pageSize * pageNumber);
            return tagMapper.toDtoList(tags);
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
            return tagMapper.toDto(tag.get());

        } catch (DaoException e) {
            throw new ServiceException("Unable to get most widely used tag of a user " +
                    "with highest spending", e, TAG_CODE);
        }
    }
}
