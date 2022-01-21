package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchElementException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.InvalidRequestDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

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
}
