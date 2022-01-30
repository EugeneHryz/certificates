package com.epam.esm.service.impl;

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
    private TagDtoMapper tagMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagDtoMapper mapper) {
        this.tagDao = tagDao;
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
        long count;
        try {
            // count all tags
            count = tagDao.getCount();
        } catch (DaoException e) {
            throw new ServiceException("Unable to count all tags", e, TAG_CODE);
        }

        // if page number is too big
        if (!validator.validatePaginationParams(pageNumber, pageSize, count)) {
            throw new InvalidRequestDataException("Invalid pagination parameters", TAG_CODE);
        }
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
}
