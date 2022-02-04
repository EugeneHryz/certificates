package com.epam.esm.web.model.converter.impl;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.model.TagRequestModel;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class TagModelConverter extends AbstractTwoWayConverter<TagRequestModel, TagDto> {

    @Override
    protected TagDto convertTo(TagRequestModel source) {
        TagDto tagDto = new TagDto();
        try {
            BeanUtils.copyProperties(tagDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // ignore ?
            e.printStackTrace();
        }
        return tagDto;
    }

    @Override
    protected TagRequestModel convertBack(TagDto source) {
        TagRequestModel tagModel = new TagRequestModel();
        try {
            BeanUtils.copyProperties(tagModel, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return tagModel;
    }
}
