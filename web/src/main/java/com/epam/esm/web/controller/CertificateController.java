package com.epam.esm.web.controller;

import com.epam.esm.repository.dao.SearchOption;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.NoSuchElementException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.exception.Error;
import com.epam.esm.web.exception.InvalidRequestDataException;
import com.epam.esm.web.validator.SearchOptionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private static final int RESOURCE_CODE = 1;

    private GiftCertificateService certificateService;

    @Autowired
    public CertificateController(GiftCertificateService certService) {
        this.certificateService = certService;
    }

    /**
     * creates GiftCertificate and tags if they are passed
     *
     * @param gcDto GiftCertificateDto to create
     * @return newly created GiftCertificateDto
     * @throws ServiceException if an error occurs
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto createGiftCertificate(@Valid @RequestBody GiftCertificateDto gcDto,
                                                    BindingResult result) throws ServiceException, InvalidRequestDataException {
        if (result.hasErrors()) {
            throw new InvalidRequestDataException(result.getAllErrors().toString());
        }
        return certificateService.createCertificate(gcDto);
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
    public GiftCertificateDto getGiftCertificate(@PathVariable int id) throws NoSuchElementException, ServiceException {
        return certificateService.getCertificate(id);
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
    public List<GiftCertificateDto> getAllCertificates(
            @RequestParam(value = "searchParam", defaultValue = "") String searchParam,
            @RequestParam(value = "tag", defaultValue = "") String tag,
            @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder) throws ServiceException, InvalidRequestDataException {

        SearchOptionValidator validator = new SearchOptionValidator();
        if (!validator.validateSortType(sortBy) || !validator.validateSortOrder(sortOrder)) {
            throw new InvalidRequestDataException("Invalid request parameter data");
        }

        SearchOption options = new SearchOption(searchParam, tag, sortBy, sortOrder);
        return certificateService.getCertificates(options);
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
    public GiftCertificateDto updateGiftCertificate(@Valid @RequestBody GiftCertificateDto certDto,
                                                    BindingResult result, @PathVariable int id)
            throws ServiceException, NoSuchElementException, InvalidRequestDataException {

        if (result.hasErrors()) {
            throw new InvalidRequestDataException(result.getAllErrors().toString());
        }
        certDto.setId(id);
        return certificateService.updateCertificate(certDto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error resourceNotFound(NoSuchElementException e) {
        return new Error(HttpStatus.NOT_FOUND.value() * 100 + RESOURCE_CODE, e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error internalServerError(ServiceException e) {
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR.value() * 100 + RESOURCE_CODE, e.getMessage());
    }

    @ExceptionHandler(InvalidRequestDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error badRequestData(InvalidRequestDataException e) {
        return new Error(HttpStatus.BAD_REQUEST.value() * 100 + RESOURCE_CODE, e.getMessage());
    }
}
