package com.epam.esm.web.model.mapper.impl;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.model.TagRequestModel;
import com.epam.esm.web.model.mapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TagModelMapper implements ModelMapper<TagDto, TagRequestModel> {

    @Override
    public TagDto toDto(TagRequestModel requestModel) {
        TagDto tagDto = new TagDto(requestModel.getName());
        tagDto.setId(requestModel.getId());
        return tagDto;
    }

    @Override
    public TagRequestModel toRequestModel(TagDto dto) {
        TagRequestModel tagModel = new TagRequestModel(dto.getId(), dto.getName());
        return tagModel;
    }
}
