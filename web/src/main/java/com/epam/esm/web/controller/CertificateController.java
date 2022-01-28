package com.epam.esm.web.controller;

import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.web.model.GiftCertificateRequestModel;
import com.epam.esm.web.model.mapper.impl.GiftCertificateModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private GiftCertificateService certificateService;

    private GiftCertificateModelMapper certificateMapper;

    @Autowired
    public CertificateController(GiftCertificateService certService,
                                 GiftCertificateModelMapper certificateMapper) {
        this.certificateService = certService;
        this.certificateMapper = certificateMapper;
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
    public GiftCertificateRequestModel createGiftCertificate(@Valid @RequestBody GiftCertificateRequestModel gcRequestModel,
                                                    BindingResult result) throws ServiceException, InvalidRequestDataException {
        if (result.hasErrors()) {
            String errorMessage = extractValidationErrorMessage(result);
            throw new InvalidRequestDataException(errorMessage);
        }
        GiftCertificateDto gcDto = certificateMapper.toDto(gcRequestModel);
        return certificateMapper.toRequestModel(certificateService.createCertificate(gcDto));
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
    public GiftCertificateRequestModel getGiftCertificate(@PathVariable int id) throws NoSuchElementException, ServiceException {
        return certificateMapper.toRequestModel(certificateService.getCertificate(id));
    }

//    /**
//     * get filtered and sorted list of GiftCertificateDto
//     *
//     * @param searchParam search certificates by part of name or description
//     * @param tag search certificates by one tag name associated with them
//     * @param sortBy specifies how certificates will be sorted (date - by last update date, name - by name)
//     * @param sortOrder specifies sort order (asc - ascending, desc - descending)
//     * @return list of GiftCertificateDto
//     * @throws ServiceException if an error occurs
//     */
    @GetMapping(produces = {"application/json"})
    public List<GiftCertificateRequestModel> getCertificates(
            @RequestParam(value = "searchParam", defaultValue = "") String searchParam,
            @RequestParam(value = "tag", defaultValue = "") String tag,
            @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestParam(value = "size", defaultValue = "2") String size) throws ServiceException, InvalidRequestDataException {

        List<GiftCertificateDto> dtoList = certificateService.getCertificates(searchParam, tag, sortBy, sortOrder, page, size);
        return certificateMapper.toRequestModelList(dtoList);
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
    public GiftCertificateRequestModel updateGiftCertificate(@Valid @RequestBody GiftCertificateDto certDto,
                                                    BindingResult result, @PathVariable int id)
            throws ServiceException, NoSuchElementException, InvalidRequestDataException {

        if (result.hasErrors()) {
            String errorMessage = extractValidationErrorMessage(result);
            throw new InvalidRequestDataException(errorMessage);
        }
        certDto.setId(id);
        return certificateMapper.toRequestModel(certificateService.updateCertificate(certDto));
    }


//    @PatchMapping("/{id}")
//    public GiftCertificateDto updateCertificateDuration(@Valid @RequestBody GiftCertificateDurationOnlyDto certDurationDto,
//                                                        BindingResult result, @PathVariable int id)
//            throws InvalidRequestDataException, ServiceException, NoSuchElementException {
//
//        if (result.hasErrors()) {
//            String errorMessage = extractValidationErrorMessage(result);
//            throw new InvalidRequestDataException(errorMessage);
//        }
//        certDurationDto.setId(id);
//        return certificateService.updateCertificateDuration(certDurationDto);
//    }

    // fixme: duplicated code
    private String extractValidationErrorMessage(BindingResult bindingResult) {
        Optional<String> message = bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage()).findFirst();

        return message.orElse("No message");
    }
}
