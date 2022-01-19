package com.epam.esm.web.validator;

import java.util.ArrayList;
import java.util.List;

public class SearchOptionValidator {

    private List<String> allowedSortTypes = new ArrayList<>();
    private List<String> allowedSortOrder = new ArrayList<>();;

    public SearchOptionValidator() {
        allowedSortTypes.add("date");
        allowedSortTypes.add("name");

        allowedSortOrder.add("asc");
        allowedSortOrder.add("desc");
    }

    public boolean validateSortType(String sortType) {
        return allowedSortTypes.stream().anyMatch(t -> t.equals(sortType));
    }

    public boolean validateSortOrder(String sortOrder) {
        return allowedSortOrder.stream().anyMatch(t -> t.equals(sortOrder));
    }
}
