package com.epam.esm.service.impl;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.impl.TagModelMapper;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.service.validator.QueryParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TagServiceImpl implements TagService {

    private TagDao tagDao;
    private TagModelMapper tagMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagModelMapper mapper) {
        this.tagDao = tagDao;
        tagMapper = mapper;
    }

    @Override
    public TagDto createTag(TagDto tagDto) throws ServiceException {
        try {
            Optional<Tag> existingTag = tagDao.findByName(tagDto.getName());
            if (existingTag.isPresent()) {
                throw new ServiceException("Tag with name '" + tagDto.getName() + "' already exists");
            }
            int generatedId = tagDao.create(tagMapper.toEntity(tagDto));

            tagDto.setId(generatedId);
            return tagDto;
        } catch (DaoException e) {
            throw new ServiceException("Unable to create new tag", e);
        }
    }

    @Override
    public TagDto getTag(int id) throws NoSuchElementException, ServiceException {
        try {
            Optional<Tag> tag = tagDao.findById(id);
            if (!tag.isPresent()) {
                throw new NoSuchElementException("Unable to get tag (id = " + id + ")");
            }
            return tagMapper.toDto(tag.get());
        } catch (DaoException e) {
            throw new ServiceException("Unable to get tag (id = " + id + ")", e);
        }
    }

    @Override
    public List<TagDto> getTags(String page, String size) throws ServiceException, InvalidRequestDataException {
        QueryParamValidator validator = new QueryParamValidator();
        if (!validator.validatePositiveInteger(page) || !validator.validatePositiveInteger(size)) {
            throw new InvalidRequestDataException("Invalid pagination parameters");
        }
        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);
        long count;
        try {
            // count all tags
            count = tagDao.getCount();
        } catch (DaoException e) {
            throw new ServiceException("Unable to count all tags", e);
        }

        // if page number is too big
        if (!validator.validatePaginationParams(pageNumber, pageSize, count)) {
            throw new InvalidRequestDataException("Invalid pagination parameters");
        }
        try {
            List<Tag> tags = tagDao.getTags(pageSize, pageSize * pageNumber);
            return tagMapper.toDtoList(tags);
        } catch (DaoException e) {
            throw new ServiceException("Unable to get tags", e);
        }
    }

    @Override
    public void deleteTag(int id) throws NoSuchElementException, ServiceException {
        try {
            if (!tagDao.deleteById(id)) {
                throw new NoSuchElementException("Unable to delete tag (id = " + id + ")");
            }
        } catch (DaoException e) {
            throw new ServiceException("Unable to delete tag (id = " + id + ")", e);
        }
    }
}
