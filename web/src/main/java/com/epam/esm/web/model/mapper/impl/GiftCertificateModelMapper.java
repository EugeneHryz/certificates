package com.epam.esm.web.model.mapper.impl;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.web.model.GiftCertificateRequestModel;
import com.epam.esm.web.model.mapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateModelMapper implements ModelMapper<GiftCertificateDto, GiftCertificateRequestModel> {

    @Autowired
    private TagModelMapper tagMapper;

    @Override
    public GiftCertificateDto toDto(GiftCertificateRequestModel requestModel) {
        GiftCertificateDto dto = new GiftCertificateDto(requestModel.getName(), requestModel.getDescription(),
                requestModel.getPrice(), requestModel.getDuration(), requestModel.getCreated(),
                requestModel.getLastUpdated(), tagMapper.toDtoList(requestModel.getTags()));
        dto.setId(requestModel.getId());
        return dto;
    }

    @Override
    public GiftCertificateRequestModel toRequestModel(GiftCertificateDto dto) {
        GiftCertificateRequestModel model = new GiftCertificateRequestModel(dto.getId(), dto.getName(), dto.getDescription(),
                dto.getPrice(), dto.getDuration(), dto.getCreated(),
                dto.getLastUpdated(), tagMapper.toRequestModelList(dto.getTags()));

        return model;
    }
}
