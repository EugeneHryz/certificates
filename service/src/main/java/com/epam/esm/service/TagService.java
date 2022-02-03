package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;

import javax.xml.ws.Service;
import java.util.List;

public interface TagService {

    TagDto createTag(TagDto tagDto) throws ServiceException;

    TagDto getTag(int id) throws NoSuchElementException, ServiceException;

    List<TagDto> getTags(int page, int size) throws ServiceException;

    void deleteTag(int id) throws NoSuchElementException, ServiceException;

    TagDto getMostWidelyUsedTagOfUserWithHighestSpending() throws ServiceException, NoSuchElementException;

    long getTagCount() throws ServiceException;
}
