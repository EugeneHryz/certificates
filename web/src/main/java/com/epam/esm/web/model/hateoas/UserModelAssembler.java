package com.epam.esm.web.model.hateoas;

import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.model.UserRequestModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserRequestModel, EntityModel<UserRequestModel>> {

    @Override
    public EntityModel<UserRequestModel> toModel(UserRequestModel entity) {
        try {
            return EntityModel.of(entity, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .getUser(entity.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .getUsers(0, 2)).withRel("users"));
        } catch (ServiceException | NoSuchElementException e) {
            // ignore
            e.printStackTrace();
        }
        return EntityModel.of(entity);
    }
}
