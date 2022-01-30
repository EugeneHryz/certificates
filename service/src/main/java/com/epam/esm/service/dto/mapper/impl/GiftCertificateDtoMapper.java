package com.epam.esm.service.dto.mapper.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateDtoMapper implements DtoMapper<GiftCertificate, GiftCertificateDto> {

    @Override
    public GiftCertificate toEntity(GiftCertificateDto dto) {
        GiftCertificate certificate = new GiftCertificate(dto.getName(), dto.getDescription(), dto.getPrice(),
                dto.getDuration(), dto.getCreated(), dto.getLastUpdated());
        certificate.setId(dto.getId());
        return certificate;
    }

    @Override
    public GiftCertificateDto toDto(GiftCertificate entity) {
        GiftCertificateDto certDto = new GiftCertificateDto(entity.getName(), entity.getDescription(), entity.getPrice(),
                entity.getDuration(), entity.getCreated(), entity.getLastUpdated(), null);
        certDto.setId(entity.getId());

        return certDto;
    }
}
