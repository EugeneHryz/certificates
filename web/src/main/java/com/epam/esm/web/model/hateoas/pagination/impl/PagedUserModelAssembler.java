package com.epam.esm.web.model.hateoas.pagination.impl;

import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.model.UserRequestModel;
import com.epam.esm.web.model.hateoas.UserModelAssembler;
import com.epam.esm.web.model.hateoas.pagination.PagedModelAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PagedUserModelAssembler implements PagedModelAssembler<UserRequestModel> {

    private final Logger logger = LoggerFactory.getLogger(PagedUserModelAssembler.class);

    private UserModelAssembler userAssembler;

    @Autowired
    public PagedUserModelAssembler(UserModelAssembler userAssembler) {
        this.userAssembler = userAssembler;
    }

    @Override
    public PagedModel<EntityModel<UserRequestModel>> toPagedModel(List<UserRequestModel> page, PagedModel.PageMetadata pageMetadata) {
        List<EntityModel<UserRequestModel>> collectionModel = page.stream()
                .map(m -> userAssembler.toModel(m)).collect(Collectors.toList());
        try {
            List<Link> links = createLinks(pageMetadata);
            return PagedModel.of(collectionModel, pageMetadata, links);

        } catch (ServiceException | InvalidRequestDataException e) {
            logger.error("error while building pagination links for UserRequestModel", e);
        }
        return PagedModel.of(collectionModel, pageMetadata);
    }

    private List<Link> createLinks(PagedModel.PageMetadata pageMetadata) throws ServiceException, InvalidRequestDataException {
        int currentPage = (int)pageMetadata.getNumber();
        int pageSize = (int)pageMetadata.getSize();
        int totalPages = (int)pageMetadata.getTotalPages();
        List<Link> links = new ArrayList<>();

        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUsers(0, pageSize)).withRel(IanaLinkRelations.FIRST));
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUsers(totalPages - 1, pageSize)).withRel(IanaLinkRelations.LAST));

        if (currentPage < totalPages && currentPage >= 0) {
            if (currentPage != 0) {
                links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUsers(currentPage - 1, pageSize)).withRel(IanaLinkRelations.PREV));
            }
            if (currentPage != totalPages - 1) {
                links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUsers(currentPage + 1, pageSize)).withRel(IanaLinkRelations.NEXT));
            }
        }
        return links;
    }
}
