package com.epam.esm.repository.dao;

public final class DatabaseColumn {

    // Gift certificate table
    public static final String CERTIFICATE_TABLE = "gift_certificate";

    public static final String CERTIFICATE_ID = "id";
    public static final String CERTIFICATE_NAME = "name";
    public static final String CERTIFICATE_DESCRIPTION = "description";
    public static final String CERTIFICATE_DURATION = "duration";
    public static final String CERTIFICATE_PRICE = "price";
    public static final String CERTIFICATE_CREATE_DATE = "create_date";
    public static final String CERTIFICATE_LAST_UPDATE_DATE = "last_update_date";

    // Tag table
    public static final String TAG_TABLE = "tag";

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";

    // cerificate-tag mapping table
    public static final String CT_MAPPING_TABLE = "certificate_tag_mapping";

    public static final String CT_MAPPING_ID = "id";
    public static final String CT_MAPPING_CERTIFICATE_ID = "certificate_id";
    public static final String CT_MAPPING_TAG_ID = "tag_id";


    private DatabaseColumn() {
    }
}
