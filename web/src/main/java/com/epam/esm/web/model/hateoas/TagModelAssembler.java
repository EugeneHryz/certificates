package com.epam.esm.web.model.hateoas;

import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.model.TagRequestModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

// todo: decide where to put assembler classes
@Component
public class TagModelAssembler implements RepresentationModelAssembler<TagRequestModel, EntityModel<TagRequestModel>> {

    @Override
    public EntityModel<TagRequestModel> toModel(TagRequestModel entity) {
        try {
            return EntityModel.of(entity, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                            .getTag(entity.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                            .getTags(0, 2)).withRel("tags"));
        } catch (NoSuchElementException | ServiceException e) {
            // ignore
            e.printStackTrace();
        }
        return EntityModel.of(entity);
    }
}
