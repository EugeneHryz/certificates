package com.epam.esm.web.model.hateoas.pagination;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface PagedModelAssembler<T> {

    PagedModel<EntityModel<T>> toPagedModel(List<T> page, PagedModel.PageMetadata pageMetadata);

    default PagedModel.PageMetadata constructPageMetadata(long totalElements, long pageNumber, long pageSize) {
        long totalPages = totalElements / pageSize;
        if (totalElements % pageSize != 0) {
            totalPages++;
        }
        return new PagedModel.PageMetadata(pageSize, pageNumber, totalElements, totalPages);
    }
}
