package com.epam.esm.repository.searchoption;

public class CertificateSearchParameter {

    private String searchParam;
    private String[] tagNames;

    private String sortBy;
    private String sortOrder;

    public CertificateSearchParameter(String searchParam, String[] tagNames, String sortBy, String sortOrder) {
        this.searchParam = searchParam;
        this.tagNames = tagNames;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public String getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }

    public String[] getTagNames() {
        return tagNames;
    }

    public void setTagNames(String[] tagNames) {
        this.tagNames = tagNames;
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
