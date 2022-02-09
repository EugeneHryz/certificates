package com.epam.esm.web.model.hateoas.pagination.impl;

import com.epam.esm.repository.searchoption.CertificateSearchParameter;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.model.GiftCertificateRequestModel;
import com.epam.esm.web.model.hateoas.CertificateModelAssembler;
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
public class PagedCertificateModelAssembler implements PagedModelAssembler<GiftCertificateRequestModel> {

    private final Logger logger = LoggerFactory.getLogger(PagedCertificateModelAssembler.class);

    private CertificateModelAssembler certificateAssembler;

    @Autowired
    public PagedCertificateModelAssembler(CertificateModelAssembler certificateAssembler) {
        this.certificateAssembler = certificateAssembler;
    }

    public PagedModel<EntityModel<GiftCertificateRequestModel>> toPagedModel(List<GiftCertificateRequestModel> page,
                                                                             PagedModel.PageMetadata pageMetadata,
                                                                             CertificateSearchParameter options) {
        PagedModel<EntityModel<GiftCertificateRequestModel>> pagedModel = toPagedModel(page, pageMetadata);
        try {
            List<Link> links = createLinks(pageMetadata, options);
            pagedModel.add(links);
            return pagedModel;
        } catch (ServiceException | InvalidRequestDataException e) {
            logger.error("error while building pagination links for GiftCertificateRequestModel", e);
        }
        return pagedModel;
    }

    @Override
    public PagedModel<EntityModel<GiftCertificateRequestModel>> toPagedModel(List<GiftCertificateRequestModel> page,
                                                                             PagedModel.PageMetadata pageMetadata) {
        List<EntityModel<GiftCertificateRequestModel>> collectionModel =
                page.stream().map(g -> certificateAssembler.toModel(g)).collect(Collectors.toList());
        return PagedModel.of(collectionModel, pageMetadata);
    }

    private List<Link> createLinks(PagedModel.PageMetadata pageMetadata, CertificateSearchParameter options)
            throws ServiceException, InvalidRequestDataException {

        int currentPage = (int)pageMetadata.getNumber();
        int pageSize = (int)pageMetadata.getSize();
        int totalPages = (int)pageMetadata.getTotalPages();
        List<Link> links = new ArrayList<>();

        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                .getCertificates(options.getSearchParam(), options.getTagNames(),
                        options.getSortBy(), options.getSortOrder(), 0, pageSize)).withRel(IanaLinkRelations.FIRST));
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                .getCertificates(options.getSearchParam(), options.getTagNames(),
                        options.getSortBy(), options.getSortOrder(), totalPages - 1,
                        pageSize)).withRel(IanaLinkRelations.LAST));

        if (currentPage < totalPages && currentPage >= 0) {
            if (currentPage != 0) {
                links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                        .getCertificates(options.getSearchParam(), options.getTagNames(),
                                options.getSortBy(), options.getSortOrder(), currentPage - 1,
                                pageSize)).withRel(IanaLinkRelations.PREV));
            }
            if (currentPage != totalPages - 1) {
                links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                        .getCertificates(options.getSearchParam(), options.getTagNames(),
                                options.getSortBy(), options.getSortOrder(), currentPage + 1,
                                pageSize)).withRel(IanaLinkRelations.NEXT));
            }
        }
        return links;
    }
}
