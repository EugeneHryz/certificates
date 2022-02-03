package com.epam.esm.web.model.hateoas;

import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.model.GiftCertificateRequestModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class CertificateModelAssembler implements RepresentationModelAssembler<GiftCertificateRequestModel,
        EntityModel<GiftCertificateRequestModel>> {

    @Override
    public EntityModel<GiftCertificateRequestModel> toModel(GiftCertificateRequestModel entity) {
        try {
            return EntityModel.of(entity, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                            .getGiftCertificate(entity.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                            .getCertificates("", new String[] {}, "date",
                                    "asc", 0, 2)).withRel("certificates"));
        } catch (ServiceException | NoSuchElementException | InvalidRequestDataException e) {
            // ignore
            e.printStackTrace();
        }
        return EntityModel.of(entity);
    }
}
