package com.epam.esm.web.model.hateoas;

import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.model.TagRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class TagModelAssembler implements RepresentationModelAssembler<TagRequestModel, EntityModel<TagRequestModel>> {

    private final Logger logger = LoggerFactory.getLogger(TagModelAssembler.class);

    @Override
    public EntityModel<TagRequestModel> toModel(TagRequestModel entity) {
        try {
            return EntityModel.of(entity, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                            .getTag(entity.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                            .getTags(0, 2)).withRel("tags"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                            .deleteTag(entity.getId())).withRel("delete"));

        } catch (NoSuchElementException | ServiceException e) {
            logger.error("error while building links for TagRequestModel", e);
        }
        return EntityModel.of(entity);
    }
}
