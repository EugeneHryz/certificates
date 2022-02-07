package com.epam.esm.web.model.converter.impl;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.model.GiftCertificateRequestModel;
import com.epam.esm.web.model.TagRequestModel;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class CertificateModelConverter extends AbstractTwoWayConverter<GiftCertificateRequestModel, GiftCertificateDto> {

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
                        // ignore ?
                        e.printStackTrace();
                    }
                    return tagDto;
                }).collect(Collectors.toList());
                certDto.setTags(tagsDto);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
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
                        e.printStackTrace();
                    }
                    return tagModel;
                }).collect(Collectors.toList());
                certModel.setTags(tagsModel);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return certModel;
    }
}
