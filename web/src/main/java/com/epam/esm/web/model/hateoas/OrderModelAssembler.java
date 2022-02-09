package com.epam.esm.web.model.hateoas;

import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.model.OrderRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<OrderRequestModel, EntityModel<OrderRequestModel>> {

    private final Logger logger = LoggerFactory.getLogger(OrderModelAssembler.class);

    @Override
    public EntityModel<OrderRequestModel> toModel(OrderRequestModel entity) {
        try {
            return EntityModel.of(entity, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .getUserOrder(entity.getUserId(), entity.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .getUserOrders(0, 2, entity.getUserId())).withRel("userOrders"));

        } catch (ServiceException | NoSuchElementException | InvalidRequestDataException e) {
            logger.error("error while building links for OrderRequestModel", e);
        }
        return EntityModel.of(entity);
    }
}
