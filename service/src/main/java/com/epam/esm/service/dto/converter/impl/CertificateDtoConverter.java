package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class CertificateDtoConverter extends AbstractTwoWayConverter<GiftCertificateDto, GiftCertificate> {

    private final Logger logger = LoggerFactory.getLogger(CertificateDtoConverter.class);

    @Override
    protected GiftCertificate convertTo(GiftCertificateDto source) {
        GiftCertificate certificate = new GiftCertificate();
        try {
            BeanUtils.copyProperties(certificate, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from GiftCertificateDto to GiftCertificate", e);
        }
        return certificate;
    }

    @Override
    protected GiftCertificateDto convertBack(GiftCertificate source) {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        try {
            BeanUtils.copyProperties(certificateDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from GiftCertificate to GiftCertificateDto", e);
        }
        return certificateDto;
    }
}
