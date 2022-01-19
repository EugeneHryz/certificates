package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchElementException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.exception.Error;
import com.epam.esm.web.exception.InvalidRequestDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tags")
public class TagController {

    private static final int RESOURCE_CODE = 2;

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * create new tag
     *
     * @param tagDto TagDto with name
     * @return created TagDto if successful
     * @throws ServiceException if an error occurs
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@Valid @RequestBody TagDto tagDto, BindingResult result) throws ServiceException,
            InvalidRequestDataException {

        if (result.hasErrors()) {
            throw new InvalidRequestDataException(result.getFieldErrors().toString());
        }
        return tagService.createTag(tagDto);
    }

    /**
     * get tag by id
     *
     * @param id tag id
     * @return TagDto
     * @throws NoSuchElementException if there's no such tag with specified id
     * @throws ServiceException if an error occurs
     */
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public TagDto getTag(@PathVariable int id) throws NoSuchElementException, ServiceException {
        return tagService.getTag(id);
    }

    /**
     * get all tags
     *
     * @return list of all tags
     * @throws ServiceException if an error occurs
     */
    @GetMapping(produces = {"application/json"})
    public List<TagDto> getAllTags() throws ServiceException {
        return tagService.getAllTags();
    }

    /**
     * delete tag by id
     *
     * @param id tag id
     * @return deleted tag id, if successful
     * @throws ServiceException if an error occurs
     * @throws NoSuchElementException if there's no such tag with specified id
     */
    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable int id) throws ServiceException, NoSuchElementException {
        tagService.deleteTag(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error resourceNotFound(NoSuchElementException e) {
        return new Error(HttpStatus.NOT_FOUND.value() * 100 + RESOURCE_CODE, e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error internalServerError(ServiceException e) {
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR.value() * 100 + RESOURCE_CODE, e.getMessage());
    }

    @ExceptionHandler(InvalidRequestDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error badRequest(InvalidRequestDataException e) {
        return new Error(HttpStatus.BAD_REQUEST.value() * 100 + RESOURCE_CODE, e.getMessage());
    }
}
