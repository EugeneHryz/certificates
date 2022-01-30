package com.epam.esm.service.dto.mapper;

import com.epam.esm.repository.entity.AbstractEntity;
import com.epam.esm.service.dto.AbstractDto;

import java.util.List;
import java.util.stream.Collectors;

public interface DtoMapper<E extends AbstractEntity, D extends AbstractDto> {

    E toEntity(D dto);

    D toDto(E entity);

    default List<E> toEntityList(List<D> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    default List<D> toDtoList(List<E> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
