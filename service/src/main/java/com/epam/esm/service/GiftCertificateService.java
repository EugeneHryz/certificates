package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto createCertificate(GiftCertificateDto gcDto) throws ServiceException;

    GiftCertificateDto getCertificate(int id) throws NoSuchElementException, ServiceException;

    List<GiftCertificateDto> getCertificates(String searchParam, String tagName, String sortBy,
                                             String sortOrder, String page, String size) throws ServiceException, InvalidRequestDataException;

    void deleteCertificate(int id) throws ServiceException, NoSuchElementException;

    GiftCertificateDto updateCertificate(GiftCertificateDto certDto) throws ServiceException, NoSuchElementException;

//    GiftCertificateDto updateCertificateDuration(GiftCertificateDurationOnlyDto certDurationDto)
//            throws ServiceException, NoSuchElementException;
}
