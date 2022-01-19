package com.epam.esm.service;

import com.epam.esm.repository.dao.SearchOption;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.NoSuchElementException;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto createCertificate(GiftCertificateDto gcDto) throws ServiceException;

    GiftCertificateDto getCertificate(int id) throws NoSuchElementException, ServiceException;

    List<GiftCertificateDto> getCertificates(SearchOption options) throws ServiceException;

    void deleteCertificate(int id) throws ServiceException, NoSuchElementException;

    GiftCertificateDto updateCertificate(GiftCertificateDto certDto) throws ServiceException, NoSuchElementException;
}
