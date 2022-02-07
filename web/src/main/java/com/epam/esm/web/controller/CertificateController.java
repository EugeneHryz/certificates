package com.epam.esm.web.controller;

import com.epam.esm.repository.searchoption.CertificateSearchParameter;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.web.model.GiftCertificateRequestModel;
import com.epam.esm.web.model.hateoas.CertificateModelAssembler;
import com.epam.esm.web.model.hateoas.pagination.impl.PagedCertificateModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private GiftCertificateService certificateService;
    private CertificateModelAssembler certificateAssembler;
    private PagedCertificateModelAssembler pagedCertificateAssembler;

    private ConversionService conversionService;

    @Autowired
    public CertificateController(GiftCertificateService certService,
                                 CertificateModelAssembler certificateAssembler,
                                 PagedCertificateModelAssembler pagedAssembler,
                                 ConversionService service) {
        this.certificateService = certService;
        this.certificateAssembler = certificateAssembler;
        this.pagedCertificateAssembler = pagedAssembler;
        conversionService = service;
    }

    /**
     * creates GiftCertificate and tags if they are passed
     *
     * @param gcDto GiftCertificateDto to create
     * @return newly created GiftCertificateDto
     * @throws ServiceException if an error occurs
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createGiftCertificate(@Valid @RequestBody GiftCertificateRequestModel gcRequestModel,
                                                BindingResult result) throws ServiceException, InvalidRequestDataException {
        if (result.hasErrors()) {
            String errorMessage = extractValidationErrorMessage(result);
            throw new InvalidRequestDataException(errorMessage, GiftCertificateServiceImpl.CERTIFICATE_CODE);
        }

        GiftCertificateDto gcDto = conversionService.convert(gcRequestModel, GiftCertificateDto.class);

        GiftCertificateRequestModel certificateModel = conversionService.convert(
                certificateService.createCertificate(gcDto), GiftCertificateRequestModel.class);

        EntityModel<GiftCertificateRequestModel> entityModel = certificateAssembler.toModel(certificateModel);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /**
     * get GiftCertificate by id with associated tags
     *
     * @param id GiftCertificate id
     * @return GiftCertificateDto
     * @throws NoSuchElementException if there's no such certificate with specified id
     * @throws ServiceException if an error occurs
     */
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public EntityModel<GiftCertificateRequestModel> getGiftCertificate(@PathVariable int id)
            throws NoSuchElementException, ServiceException {

        GiftCertificateRequestModel certificateModel = conversionService
                .convert(certificateService.getCertificate(id), GiftCertificateRequestModel.class);
        return certificateAssembler.toModel(certificateModel);
    }

    /**
     * get filtered and sorted list of GiftCertificateDto
     *
     * @param searchParam search certificates by part of name or description
     * @param tag search certificates by one tag name associated with them
     * @param sortBy specifies how certificates will be sorted (date - by last update date, name - by name)
     * @param sortOrder specifies sort order (asc - ascending, desc - descending)
     * @return list of GiftCertificateDto
     * @throws ServiceException if an error occurs
     */
    @GetMapping(produces = {"application/json"})
    public PagedModel<EntityModel<GiftCertificateRequestModel>> getCertificates(
            @RequestParam(value = "searchParam", defaultValue = "") String searchParam,
            @RequestParam(value = "tags", defaultValue = "") String[] tags,
            @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "2") int size) throws ServiceException, InvalidRequestDataException {

        // todo: validate this?
        CertificateSearchParameter searchParameter = new CertificateSearchParameter(searchParam, tags, sortBy, sortOrder);

        List<GiftCertificateDto> dtoList = certificateService.getCertificates(searchParameter, page, size);
        List<GiftCertificateRequestModel> requestModelList = dtoList.stream()
                .map(r -> conversionService.convert(r, GiftCertificateRequestModel.class)).collect(Collectors.toList());

        long totalCount = certificateService.getCertificateCount(searchParameter);
        PagedModel.PageMetadata pageMetadata = pagedCertificateAssembler.constructPageMetadata(totalCount, page, size);
        return pagedCertificateAssembler.toPagedModel(requestModelList, pageMetadata, searchParameter);
    }

    /**
     * delete certificate with specified id
     *
     * @param id certificate id
     * @return deleted certificate id, if successful
     * @throws ServiceException if an error occurs
     * @throws NoSuchElementException if there's no such certificate with specified id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable int id) throws ServiceException, NoSuchElementException {
        certificateService.deleteCertificate(id);
    }

    /**
     * update existing gift certificate
     *
     * @param certDto GiftCertificateDto with values that need to be updated
     * @param id certificate id
     * @return updated GiftCertificateDto
     * @throws ServiceException if an error occurs
     * @throws NoSuchElementException if there's no such certificate with specified id
     */
    @PutMapping("/{id}")
    public EntityModel<GiftCertificateRequestModel> updateGiftCertificate(@Valid @RequestBody GiftCertificateRequestModel certRequestModel,
                                                    BindingResult result, @PathVariable int id)
            throws ServiceException, NoSuchElementException, InvalidRequestDataException {

        if (result.hasErrors()) {
            String errorMessage = extractValidationErrorMessage(result);
            throw new InvalidRequestDataException(errorMessage, GiftCertificateServiceImpl.CERTIFICATE_CODE);
        }
        certRequestModel.setId(id);
        GiftCertificateDto certDto = conversionService.convert(certRequestModel, GiftCertificateDto.class);
        GiftCertificateDto updatedDto = certificateService.updateCertificate(certDto);
        GiftCertificateRequestModel certModel = conversionService.convert(updatedDto, GiftCertificateRequestModel.class);

        return certificateAssembler.toModel(certModel);
    }

    // fixme: duplicated code
    private String extractValidationErrorMessage(BindingResult bindingResult) {
        Optional<String> message = bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage()).findFirst();

        return message.orElse("No message");
    }
}
