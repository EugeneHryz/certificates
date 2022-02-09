package com.epam.esm.web.model.converter.impl;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.model.GiftCertificateRequestModel;
import com.epam.esm.web.model.TagRequestModel;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class CertificateModelConverter extends AbstractTwoWayConverter<GiftCertificateRequestModel, GiftCertificateDto> {

    private final Logger logger = LoggerFactory.getLogger(CertificateModelConverter.class);

    @Override
    protected GiftCertificateDto convertTo(GiftCertificateRequestModel source) {
        GiftCertificateDto certDto = new GiftCertificateDto();
        try {
            BeanUtils.copyProperties(certDto, source);

            if (source.getTags() != null) {
                List<TagDto> tagsDto = source.getTags().stream().map(t -> {
                    TagDto tagDto = new TagDto();
                    try {
                        BeanUtils.copyProperties(tagDto, t);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("error while converting from TagRequestModel to TagDto", e);
                    }
                    return tagDto;
                }).collect(Collectors.toList());
                certDto.setTags(tagsDto);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from GiftCertificateRequestModel to GiftCertificateDto", e);
        }
        return certDto;
    }

    @Override
    protected GiftCertificateRequestModel convertBack(GiftCertificateDto source) {
        GiftCertificateRequestModel certModel = new GiftCertificateRequestModel();
        try {
            BeanUtils.copyProperties(certModel, source);

            if (source.getTags() != null) {
                List<TagRequestModel> tagsModel = source.getTags().stream().map(t -> {
                    TagRequestModel tagModel = new TagRequestModel();
                    try {
                        BeanUtils.copyProperties(tagModel, t);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("error while converting from TagDto to TagRequestModel", e);
                    }
                    return tagModel;
                }).collect(Collectors.toList());
                certModel.setTags(tagsModel);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from GiftCertificateDto to GiftCertificateRequestModel", e);
        }
        return certModel;
    }
}
