package com.epam.esm.web.model.hateoas;

import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.model.GiftCertificateRequestModel;
import com.epam.esm.web.model.converter.impl.CertificateModelConverter;
import com.epam.esm.web.model.converter.impl.UserModelConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResultUtils;

@Component
public class CertificateModelAssembler implements RepresentationModelAssembler<GiftCertificateRequestModel,
        EntityModel<GiftCertificateRequestModel>> {

    private final Logger logger = LoggerFactory.getLogger(CertificateModelAssembler.class);

    @Override
    public EntityModel<GiftCertificateRequestModel> toModel(GiftCertificateRequestModel entity) {
        try {
            return EntityModel.of(entity, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                            .getGiftCertificate(entity.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                            .getCertificates("", new String[] {}, "date",
                                    "asc", 0, 2)).withRel("certificates"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                            .updateGiftCertificate(entity, null, entity.getId())).withRel("update"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                            .deleteCertificate(entity.getId())).withRel("delete"));

        } catch (ServiceException | NoSuchElementException | InvalidRequestDataException e) {
            logger.error("error while building links for GiftCertificateRequestModel", e);
        }
        return EntityModel.of(entity);
    }
}
