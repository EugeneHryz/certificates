package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.web.model.TagRequestModel;
import com.epam.esm.web.model.mapper.impl.TagModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    private TagService tagService;

    private TagModelMapper tagMapper;

    @Autowired
    public TagController(TagService tagService, TagModelMapper tagMapper) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    /**
     * create new tag
     *
     * @param
     * @return created TagDto if successful
     * @throws ServiceException if an error occurs
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public TagRequestModel createTag(@Valid @RequestBody TagRequestModel tagRequestModel, BindingResult result)
            throws ServiceException, InvalidRequestDataException {

        if (result.hasErrors()) {
            String errorMessage = extractValidationErrorMessage(result);
            throw new InvalidRequestDataException(errorMessage, TagServiceImpl.TAG_CODE);
        }
        TagDto tagDto = tagMapper.toDto(tagRequestModel);
        return tagMapper.toRequestModel(tagService.createTag(tagDto));
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
    public TagRequestModel getTag(@PathVariable int id) throws NoSuchElementException, ServiceException {
        return tagMapper.toRequestModel(tagService.getTag(id));
    }

    @GetMapping(produces = {"application/json"})
    public List<TagRequestModel> getTags(@RequestParam(value = "page", defaultValue = "0") String page,
                                @RequestParam(value = "size", defaultValue = "2") String size)
            throws ServiceException, InvalidRequestDataException {

        List<TagDto> tagsDto = tagService.getTags(page, size);
        return tagMapper.toRequestModelList(tagsDto);
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

    private String extractValidationErrorMessage(BindingResult bindingResult) {
        Optional<String> message = bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage()).findFirst();

        return message.orElse("No message");
    }
}
