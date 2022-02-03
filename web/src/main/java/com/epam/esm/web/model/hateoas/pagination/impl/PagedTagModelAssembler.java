package com.epam.esm.web.model.hateoas.pagination.impl;

import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.model.TagRequestModel;
import com.epam.esm.web.model.hateoas.TagModelAssembler;
import com.epam.esm.web.model.hateoas.pagination.PagedModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PagedTagModelAssembler implements PagedModelAssembler<TagRequestModel> {

    private TagModelAssembler tagAssembler;

    @Autowired
    public PagedTagModelAssembler(TagModelAssembler tagAssembler) {
        this.tagAssembler = tagAssembler;
    }

    @Override
    public PagedModel<EntityModel<TagRequestModel>> toPagedModel(List<TagRequestModel> page,
                                                                 PagedModel.PageMetadata pageMetadata) {
        List<EntityModel<TagRequestModel>> collectionModel = page.stream()
                .map(m -> tagAssembler.toModel(m)).collect(Collectors.toList());
        try {
            List<Link> links = createLinks(pageMetadata);
            return PagedModel.of(collectionModel, pageMetadata, links);

        } catch (ServiceException | InvalidRequestDataException e) {
            // ignore
            e.printStackTrace();
        }
        return PagedModel.of(collectionModel, pageMetadata);
    }

    private List<Link> createLinks(PagedModel.PageMetadata pageMetadata) throws ServiceException, InvalidRequestDataException {
        int currentPage = (int)pageMetadata.getNumber();
        int pageSize = (int)pageMetadata.getSize();
        int totalPages = (int)pageMetadata.getTotalPages();
        List<Link> links = new ArrayList<>();

        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                .getTags(0, pageSize)).withRel(IanaLinkRelations.FIRST));
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                .getTags(totalPages - 1, pageSize)).withRel(IanaLinkRelations.LAST));

        if (currentPage < totalPages && currentPage >= 0) {
            if (currentPage != 0) {
                links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getTags(currentPage - 1, pageSize)).withRel(IanaLinkRelations.PREV));
            }
            if (currentPage != totalPages - 1) {
                links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getTags(currentPage + 1, pageSize)).withRel(IanaLinkRelations.NEXT));
            }
        }
        return links;
    }
}
