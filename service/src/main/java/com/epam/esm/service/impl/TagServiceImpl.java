package com.epam.esm.service.impl;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchElementException;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TagServiceImpl implements TagService {

    private TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public TagDto createTag(TagDto tagDto) throws ServiceException {
        try {
            Optional<Tag> existingTag = tagDao.findByName(tagDto.getName());
            if (existingTag.isPresent()) {
                throw new ServiceException("Tag with name '" + tagDto.getName() + "' already exists");
            }
            int generatedId = tagDao.create(new Tag(tagDto.getName()));

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
            return new TagDto(tag.get());
        } catch (DaoException e) {
            throw new ServiceException("Unable to get tag (id = " + id + ")", e);
        }
    }

    @Override
    public List<TagDto> getAllTags() throws ServiceException {
        try {
            List<Tag> tags = tagDao.findAll();
            return tags.stream().map(TagDto::new).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Unable to get all tags", e);
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
