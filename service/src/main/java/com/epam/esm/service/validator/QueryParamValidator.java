package com.epam.esm.service.validator;

import java.util.ArrayList;
import java.util.List;

// todo: refactor this validator
public class QueryParamValidator {

    private static final String POSITIVE_INTEGER_REGEX = "^[0-9]{1,9}$";

    private List<String> allowedSortTypes = new ArrayList<>();
    private List<String> allowedSortOrder = new ArrayList<>();

    public QueryParamValidator() {
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

    public boolean validatePositiveInteger(String number) {
        return number.matches(POSITIVE_INTEGER_REGEX);
    }

//    public boolean validatePaginationParams(int pageNumber, int pageSize, long elementCount) {
//        boolean result = false;
//        if (pageSize != 0) {
//            int numberOfPages = (int)(elementCount / pageSize);
//            if (elementCount % pageSize != 0) {
//                numberOfPages++;
//            }
//            result = pageNumber < numberOfPages;
//        }
//        return result;
//    }
}
