package com.epam.esm.web.model.hateoas.pagination.impl;

import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.model.OrderRequestModel;
import com.epam.esm.web.model.hateoas.OrderModelAssembler;
import com.epam.esm.web.model.hateoas.pagination.PagedModelAssembler;
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
public class PagedOrderModelAssembler implements PagedModelAssembler<OrderRequestModel> {

    private OrderModelAssembler orderAssembler;

    @Autowired
    public PagedOrderModelAssembler(OrderModelAssembler orderAssembler) {
        this.orderAssembler = orderAssembler;
    }

    public PagedModel<EntityModel<OrderRequestModel>> toPagedModel(List<OrderRequestModel> page,
                                                                   PagedModel.PageMetadata pageMetadata,
                                                                   int userId) {
        PagedModel<EntityModel<OrderRequestModel>> pagedModel = toPagedModel(page, pageMetadata);
        try {
            List<Link> links = createLinks(pageMetadata, userId);
            pagedModel.add(links);
            return pagedModel;
        } catch (ServiceException | InvalidRequestDataException | NoSuchElementException e) {
            // ignore
            e.printStackTrace();
        }
        return pagedModel;
    }

    @Override
    public PagedModel<EntityModel<OrderRequestModel>> toPagedModel(List<OrderRequestModel> page, PagedModel.PageMetadata pageMetadata) {

        List<EntityModel<OrderRequestModel>> collectionModel = page.stream()
                .map(o -> orderAssembler.toModel(o)).collect(Collectors.toList());
        return PagedModel.of(collectionModel, pageMetadata);
    }

    private List<Link> createLinks(PagedModel.PageMetadata pageMetadata, int userId) throws ServiceException, InvalidRequestDataException, NoSuchElementException {
        int currentPage = (int)pageMetadata.getNumber();
        int pageSize = (int)pageMetadata.getSize();
        int totalPages = (int)pageMetadata.getTotalPages();
        List<Link> links = new ArrayList<>();

        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserOrders(0, pageSize, userId)).withRel(IanaLinkRelations.FIRST));
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserOrders(totalPages - 1, pageSize, userId)).withRel(IanaLinkRelations.LAST));

        if (currentPage < totalPages && currentPage >= 0) {
            if (currentPage != 0) {
                links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUserOrders(currentPage - 1, pageSize, userId)).withRel(IanaLinkRelations.PREV));
            }
            if (currentPage != totalPages - 1) {
                links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUserOrders(currentPage + 1, pageSize, userId)).withRel(IanaLinkRelations.NEXT));
            }
        }
        return links;
    }
}
