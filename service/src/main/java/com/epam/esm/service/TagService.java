package com.epam.esm.service;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchElementException;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface TagService {

    TagDto createTag(TagDto tagDto) throws ServiceException;

    TagDto getTag(int id) throws NoSuchElementException, ServiceException;

    List<TagDto> getAllTags() throws ServiceException;

    void deleteTag(int id) throws NoSuchElementException, ServiceException;
}
