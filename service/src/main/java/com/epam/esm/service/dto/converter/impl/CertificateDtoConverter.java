package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class CertificateDtoConverter extends AbstractTwoWayConverter<GiftCertificateDto, GiftCertificate> {

    private final Logger logger = LoggerFactory.getLogger(CertificateDtoConverter.class);

    @Override
    protected GiftCertificate convertTo(GiftCertificateDto source) {
        GiftCertificate certificate = new GiftCertificate();
        try {
            BeanUtils.copyProperties(certificate, source);

            if (source.getTags() != null) {
                List<Tag> tags = source.getTags().stream().map(t -> {
                    Tag tag = new Tag();
                    try {
                        BeanUtils.copyProperties(tag, t);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("error while converting from TagDto to Tag", e);
                    }
                    return tag;
                }).collect(Collectors.toList());
                certificate.setTags(tags);
            }
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

            if (source.getTags() != null) {
                List<TagDto> tagsDto = source.getTags().stream().map(t -> {
                    TagDto tagDto = new TagDto();
                    try {
                        BeanUtils.copyProperties(tagDto, t);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("error while converting from Tag to TagDto", e);
                    }
                    return tagDto;
                }).collect(Collectors.toList());
                certificateDto.setTags(tagsDto);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from GiftCertificate to GiftCertificateDto", e);
        }
        return certificateDto;
    }
}
