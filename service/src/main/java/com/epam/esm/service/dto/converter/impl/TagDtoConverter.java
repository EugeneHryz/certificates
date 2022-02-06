package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class TagDtoConverter extends AbstractTwoWayConverter<TagDto, Tag> {

    @Override
    protected Tag convertTo(TagDto source) {
        Tag tag = new Tag();
        try {
            BeanUtils.copyProperties(tag, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return tag;
    }

    @Override
    protected TagDto convertBack(Tag source) {
        TagDto tagDto = new TagDto();
        try {
            BeanUtils.copyProperties(tagDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return tagDto;
    }
}
