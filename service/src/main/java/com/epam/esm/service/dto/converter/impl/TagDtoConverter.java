package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

public class TagDtoConverter extends AbstractTwoWayConverter<TagDto, Tag> {

    private final Logger logger = LoggerFactory.getLogger(TagDtoConverter.class);

    @Override
    protected Tag convertTo(TagDto source) {
        Tag tag = new Tag();
        try {
            BeanUtils.copyProperties(tag, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from TagDto to Tag", e);
        }
        return tag;
    }

    @Override
    protected TagDto convertBack(Tag source) {
        TagDto tagDto = new TagDto();
        try {
            BeanUtils.copyProperties(tagDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from Tag to TagDto", e);
        }
        return tagDto;
    }
}
