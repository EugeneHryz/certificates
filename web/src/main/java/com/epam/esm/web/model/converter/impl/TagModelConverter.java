package com.epam.esm.web.model.converter.impl;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.model.TagRequestModel;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

public class TagModelConverter extends AbstractTwoWayConverter<TagRequestModel, TagDto> {

    private final Logger logger = LoggerFactory.getLogger(TagModelConverter.class);

    @Override
    protected TagDto convertTo(TagRequestModel source) {
        TagDto tagDto = new TagDto();
        try {
            BeanUtils.copyProperties(tagDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from TagRequestModel to TagDto", e);
        }
        return tagDto;
    }

    @Override
    protected TagRequestModel convertBack(TagDto source) {
        TagRequestModel tagModel = new TagRequestModel();
        try {
            BeanUtils.copyProperties(tagModel, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from TagDto to TagRequestModel", e);
        }
        return tagModel;
    }
}
