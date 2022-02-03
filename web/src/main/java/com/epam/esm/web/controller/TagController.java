package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.web.model.TagRequestModel;
import com.epam.esm.web.model.hateoas.TagModelAssembler;
import com.epam.esm.web.model.hateoas.pagination.impl.PagedTagModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tags")
public class TagController {

    private TagService tagService;
    private TagModelAssembler tagAssembler;
    private PagedTagModelAssembler pagedTagAssembler;

    private ConversionService conversionService;

    @Autowired
    public TagController(TagService tagService, TagModelAssembler tagAssembler,
                         PagedTagModelAssembler pagedTagAssembler, ConversionService conversionService) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
        this.pagedTagAssembler = pagedTagAssembler;
        this.conversionService = conversionService;
    }

    /**
     * create new tag
     *
     * @param
     * @return created TagDto if successful
     * @throws ServiceException if an error occurs
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createTag(@Valid @RequestBody TagRequestModel tagRequestModel, BindingResult result)
            throws ServiceException, InvalidRequestDataException {

        if (result.hasErrors()) {
            String errorMessage = extractValidationErrorMessage(result);
            throw new InvalidRequestDataException(errorMessage, TagServiceImpl.TAG_CODE);
        }
        TagDto tagDto = conversionService.convert(tagRequestModel, TagDto.class);
        TagRequestModel tagModel = conversionService.convert(tagService.createTag(tagDto), TagRequestModel.class);

        EntityModel<TagRequestModel> entityModel = tagAssembler.toModel(tagModel);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
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
    public EntityModel<TagRequestModel> getTag(@PathVariable int id) throws NoSuchElementException, ServiceException {
        TagRequestModel tagRequestModel = conversionService.convert(tagService.getTag(id), TagRequestModel.class);
        return tagAssembler.toModel(tagRequestModel);
    }

    @GetMapping(produces = {"application/json"})
    public PagedModel<EntityModel<TagRequestModel>> getTags(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "2") int size)
            throws ServiceException {

        List<TagDto> tagsDto = tagService.getTags(page, size);
        List<TagRequestModel> tagsModel = tagsDto.stream()
                .map(t -> conversionService.convert(t, TagRequestModel.class)).collect(Collectors.toList());

        long totalElements = tagService.getTagCount();
        PagedModel.PageMetadata pageMetadata = pagedTagAssembler.constructPageMetadata(totalElements, page, size);
        return pagedTagAssembler.toPagedModel(tagsModel, pageMetadata);
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

    @GetMapping(value = "/widelyUsedTag", produces = {"application/json"})
    public EntityModel<TagRequestModel> getMostWidelyUsedTagOfUserWithMostSpending()
            throws ServiceException, NoSuchElementException {

        TagDto tagDto = tagService.getMostWidelyUsedTagOfUserWithHighestSpending();
        TagRequestModel tagRequestModel = conversionService.convert(tagDto, TagRequestModel.class);

        return tagAssembler.toModel(tagRequestModel);
    }

    private String extractValidationErrorMessage(BindingResult bindingResult) {
        Optional<String> message = bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage()).findFirst();

        return message.orElse("No message");
    }
}
