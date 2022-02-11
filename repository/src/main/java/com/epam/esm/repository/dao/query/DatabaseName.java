package com.epam.esm.repository.dao.query;

public final class DatabaseName {

    // Gift certificate table
    public static final String CERTIFICATE_TABLE = "gift_certificate";
    public static final String CERTIFICATE_ID = "id";
    public static final String CERTIFICATE_NAME = "name";
    public static final String CERTIFICATE_DESCRIPTION = "description";
    public static final String CERTIFICATE_DURATION = "duration";
    public static final String CERTIFICATE_PRICE = "price";
    public static final String CERTIFICATE_CREATED_DATE = "created";
    public static final String CERTIFICATE_LAST_UPDATED_DATE = "last_updated";

    // Tag table
    public static final String TAG_TABLE = "tag";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";

    // cerificate-tag mapping table
    public static final String CT_MAPPING_TABLE = "certificate_tag_mapping";
    public static final String CT_MAPPING_ID = "id";
    public static final String CT_MAPPING_CERTIFICATE_ID = "certificate_id";
    public static final String CT_MAPPING_TAG_ID = "tag_id";

    // user table
    public static final String USER_TABLE = "user";
    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";

    // order table
    public static final String ORDER_TABLE = "certificate_order";
    public static final String ORDER_ID = "id";
    public static final String ORDER_USER_ID = "user_id";
    public static final String ORDER_CERTIFICATE_ID = "certificate_id";
    public static final String ORDER_TOTAL = "total";
    public static final String ORDER_PURCHASE_DATE = "purchase_date";

    private DatabaseName() {
    }
}
