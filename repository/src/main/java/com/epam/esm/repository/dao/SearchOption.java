package com.epam.esm.repository.dao;

public class SearchOption {

    private String searchParam;
    private String tagName;

    private String sortBy;
    private String sortOrder;

    public SearchOption(String searchParam, String tagName, String sortBy, String sortOrder) {
        this.searchParam = searchParam;
        this.tagName = tagName;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public String getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
