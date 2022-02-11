package com.epam.esm.service;

import com.epam.esm.repository.searchoption.CertificateSearchParameter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto createCertificate(GiftCertificateDto gcDto) throws ServiceException, InvalidRequestDataException;

    GiftCertificateDto getCertificate(int id) throws NoSuchElementException, ServiceException;

    List<GiftCertificateDto> findCertificates(CertificateSearchParameter options, int page, int size) throws ServiceException, InvalidRequestDataException;

    void deleteCertificate(int id) throws ServiceException, NoSuchElementException;

    GiftCertificateDto updateCertificate(GiftCertificateDto certDto) throws ServiceException, NoSuchElementException, InvalidRequestDataException;

    long getCertificateCount(CertificateSearchParameter options) throws ServiceException, InvalidRequestDataException;
}
