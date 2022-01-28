package com.epam.esm.service.dto.mapper.impl;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TagModelMapper implements ModelMapper<Tag, TagDto> {

    @Override
    public Tag toEntity(TagDto dto) {
        Tag tag = new Tag(dto.getName());
        tag.setId(dto.getId());
        return tag;
    }

    @Override
    public TagDto toDto(Tag entity) {
        TagDto tagDto = new TagDto(entity.getName());
        tagDto.setId(tagDto.getId());
        return tagDto;
    }
}
