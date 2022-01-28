package com.epam.esm.web.model.mapper;

import com.epam.esm.service.dto.AbstractDto;

import java.util.List;
import java.util.stream.Collectors;

public interface ModelMapper<D extends AbstractDto, T> {

    D toDto(T requestModel);

    T toRequestModel(D dto);

    default List<D> toDtoList(List<T> requestModelList) {
        return requestModelList.stream().map(this::toDto).collect(Collectors.toList());
    }

    default List<T> toRequestModelList(List<D> dtoList) {
        return dtoList.stream().map(this::toRequestModel).collect(Collectors.toList());
    }
}
