package com.valevich.kursach.storage;

class ShopContract {

    // category table
    static final String CATEGORY_TABLE_NAME = "category";
    static final String CATEGORY_NAME_COLUMN = "name";
    static final String CATEGORY_IMAGE_COLUMN = "imageUrl";
    static final String CATEGORY_ID_COLUMN = "id";
    //client table
    static final String CLIENTS_TABLE_NAME = "client";
    static final String CLIENT_ID_COLUMN = "id";
    static final String CLIENT_TOKEN_COLUMN = "token";
    static final String CLIENT_EMAIL_COLUMN = "email";
    private static final String CLIENT_PASSWORD_COLUMN = "password";
    static final String CLIENT_ADDRESS_COLUMN = "address";
    static final String CLIENT_PHONE_COLUMN = "phone_number";
    static final String CLIENT_NAME_COLUMN = "name";
    static final String CLIENT_SURNAME_COLUMN = "surname";
    //employee table
    static final String EMPLOYEES_TABLE_NAME = "employee";
    static final String EMPLOYEE_ID_COLUMN = "id";
    static final String EMPLOYEE_NAME_COLUMN = "name";
    static final String EMPLOYEE_SURNAME_COLUMN = "surname";
    static final String EMPLOYEE_PHONE_COLUMN = "phone_number";
    static final String EMPLOYEE_AGE_COLUMN = "age";
    static final String EMPLOYEE_POSITION_COLUMN = "position";
    static final String EMPLOYEE_EMAIL_COLUMN = "email";
    //product table
    static final String PRODUCT_TABLE_NAME = "product";
    static final String PRODUCT_TITLE_COLUMN = "title";
    static final String PRODUCT_ID_COLUMN = "id";
    static final String PRODUCT_FEATURES_COLUMN = "features";
    static final String PRODUCT_STOCK_AMOUNT_COLUMN = "stock_amount";
    static final String PRODUCT_PRICE_COLUMN = "price";
    static final String PRODUCT_METRICS_COLUMN = "metrics";
    static final String PRODUCT_IMAGE_URL = "imageUrl";
    static final String PRODUCT_DESCRIPTION_COLUMN = "description";
    static final String PRODUCT_ARTICUL_COLUMN = "articul";
    static final String PRODUCT_CATEGORY_ID_COLUMN = "categoryId";
    static final String PRODUCT_STOCK_ID_COLUMN = "stockId";
    //order table
    static final String ORDER_TABLE_NAME = "orders";
    static final String ORDER_ID_COLUMN = "id";
    static final String ORDER_DATE_COLUMN = "date";
    static final String ORDER_CLIENT_ID_COLUMN = "client_id";
    static final String ORDER_STATUS_ID_COLUMN = "status_id";
    static final String ORDER_EMPLOYEE_ID_COLUMN = "employee_id";
    static final String ORDER_PHONE_NUMBER_COLUMN = "phone_number";
    static final String ORDER_ADDRESS_COLUMN = "address";
    static final String ORDER_CLIENT_NAME_COLUMN = "client_name";
    static final String ORDER_SUM_COLUMN = "sum";
    //orderproduct table
    private static final String ORDER_PRODUCT_TABLE_NAME = "orderproduct";
    private static final String ORDER_PRODUCT_ORDER_ID_COLUMN = "orderId";
    static final String ORDER_PRODUCT_PRODUCT_ID_COLUMN = "productId";
    static final String ORDER_PRODUCT_AMOUNT_COLUMN = "amount";
    //orderstatus table
    static final String STATUS_TABLE_NAME = "orderstatus";
    static final String STATUS_ID_COLUMN = "id";
    static final String STATUS_DESCRIPTION_COLUMN = "description";
    //stock table
    static final String STOCK_TABLE_NAME = "stock";
    static final String STOCK_ID_COLUMN = "id";
    static final String STOCK_ADDRESS_COLUMN = "address";


    static final String ADD_CATEGORY = "INSERT INTO "
            + CATEGORY_TABLE_NAME
            + " (" + CATEGORY_NAME_COLUMN + "," + CATEGORY_IMAGE_COLUMN + ")"
            + " VALUES"
            + "(?,?)";

    static final String UPDATE_CATEGORY = "UPDATE "
            + CATEGORY_TABLE_NAME
            + " SET " + CATEGORY_NAME_COLUMN + " = ?"
            + "," + CATEGORY_IMAGE_COLUMN + " = ?"
            + " WHERE " + CATEGORY_ID_COLUMN + " = ?";

    static final String REMOVE_CATEGORY = "DELETE FROM "
            + CATEGORY_TABLE_NAME
            + " WHERE " + CATEGORY_ID_COLUMN + " = ?";

    static final String GET_CATALOG = "SELECT * FROM " + PRODUCT_TABLE_NAME
            + " INNER JOIN " + STOCK_TABLE_NAME
            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_STOCK_ID_COLUMN
            + " = " + STOCK_TABLE_NAME + "." + STOCK_ID_COLUMN
            + " RIGHT JOIN " + CATEGORY_TABLE_NAME
            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_CATEGORY_ID_COLUMN
            + " = " + CATEGORY_TABLE_NAME + "." + CATEGORY_ID_COLUMN
            + " ORDER BY " + PRODUCT_CATEGORY_ID_COLUMN;

    static final String GET_ALL_TOKENS = "SELECT " + CLIENT_TOKEN_COLUMN
            + " FROM " + CLIENTS_TABLE_NAME
            + " WHERE " + CLIENT_TOKEN_COLUMN + " = ?";

    static final String ADD_CLIENT = "INSERT INTO "
            + CLIENTS_TABLE_NAME
            + " (" + CLIENT_EMAIL_COLUMN
            + "," + CLIENT_PASSWORD_COLUMN
            + "," + CLIENT_TOKEN_COLUMN + ")"
            + " VALUES"
            + " (?,?,?)";

    static final String ADD_CLIENT_ADMIN = "INSERT INTO "
            + CLIENTS_TABLE_NAME
            + " (" + CLIENT_NAME_COLUMN
            + "," + CLIENT_SURNAME_COLUMN
            + "," + CLIENT_PHONE_COLUMN
            + "," + CLIENT_ADDRESS_COLUMN
            + "," + CLIENT_EMAIL_COLUMN
            + "," + CLIENT_PASSWORD_COLUMN
            + "," + CLIENT_TOKEN_COLUMN + ")"
            + " VALUES"
            + "(?,?,?,?,?,?,?)";

    static final String UPDATE_CLIENT_ADMIN = "UPDATE "
            + CLIENTS_TABLE_NAME
            + " SET " + CLIENT_NAME_COLUMN + " =?"
            + "," + CLIENT_SURNAME_COLUMN + " =?"
            + "," + CLIENT_PHONE_COLUMN + " =?"
            + "," + CLIENT_ADDRESS_COLUMN + " =?"
            + "," + CLIENT_EMAIL_COLUMN + " =?"
            + "," + CLIENT_PASSWORD_COLUMN + " =?"
            + " WHERE " + CLIENT_ID_COLUMN + " =?";

    static final String UPDATE_CLIENT_CLIENT = "UPDATE "
            + CLIENTS_TABLE_NAME
            + " SET " + CLIENT_NAME_COLUMN + " =?"
            + "," + CLIENT_SURNAME_COLUMN + " =?"
            + "," + CLIENT_PHONE_COLUMN + " =?"
            + "," + CLIENT_ADDRESS_COLUMN + " =?"
            + "," + CLIENT_EMAIL_COLUMN + " =?"
            + "," + CLIENT_PASSWORD_COLUMN + " =?"
            + " WHERE " + CLIENT_TOKEN_COLUMN + " =?";

    static final String DELETE_CLIENT = "DELETE FROM " + CLIENTS_TABLE_NAME
            + " WHERE " + CLIENT_ID_COLUMN + " =?";

    static final String GET_CLIENTS_WITH_ORDERS = "SELECT * FROM " + PRODUCT_TABLE_NAME
            + " INNER JOIN " + STOCK_TABLE_NAME
            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_STOCK_ID_COLUMN
            + " = " + STOCK_TABLE_NAME + "." + STOCK_ID_COLUMN
            + " INNER JOIN " + ORDER_PRODUCT_TABLE_NAME
            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_ID_COLUMN
            + " = " + ORDER_PRODUCT_TABLE_NAME + "." + ORDER_PRODUCT_PRODUCT_ID_COLUMN
            + " INNER JOIN " + ORDER_TABLE_NAME
            + " ON " + ORDER_TABLE_NAME + "." + ORDER_ID_COLUMN
            + " = " + ORDER_PRODUCT_TABLE_NAME + "." + ORDER_PRODUCT_ORDER_ID_COLUMN
            + " INNER JOIN " + STATUS_TABLE_NAME
            + " ON " + ORDER_TABLE_NAME + "." + ORDER_STATUS_ID_COLUMN
            + " = " + STATUS_TABLE_NAME + "." + STATUS_ID_COLUMN
            + " LEFT JOIN " + EMPLOYEES_TABLE_NAME
            + " ON " + ORDER_TABLE_NAME + "." + ORDER_EMPLOYEE_ID_COLUMN
            + " = " + EMPLOYEES_TABLE_NAME + "." + EMPLOYEE_ID_COLUMN
            + " RIGHT JOIN " + CLIENTS_TABLE_NAME
            + " ON " + CLIENTS_TABLE_NAME + "." + CLIENT_ID_COLUMN
            + " = " + ORDER_TABLE_NAME + "." + ORDER_CLIENT_ID_COLUMN;


    static final String GET_CLIENT_ORDERS = "SELECT * FROM " + ORDER_TABLE_NAME
            + " INNER JOIN " + STATUS_TABLE_NAME
            + " ON " + ORDER_TABLE_NAME + "." + ORDER_STATUS_ID_COLUMN
            + " = " + STATUS_TABLE_NAME + "." + STATUS_ID_COLUMN
            + " LEFT JOIN " + EMPLOYEES_TABLE_NAME
            + " ON " + ORDER_TABLE_NAME + "." + ORDER_EMPLOYEE_ID_COLUMN
            + " = " + EMPLOYEES_TABLE_NAME + "." + EMPLOYEE_ID_COLUMN
            + " INNER JOIN " + ORDER_PRODUCT_TABLE_NAME
            + " ON " + ORDER_TABLE_NAME + "." + ORDER_ID_COLUMN
            + " = " + ORDER_PRODUCT_TABLE_NAME + "." + ORDER_PRODUCT_ORDER_ID_COLUMN
            + " INNER JOIN " + PRODUCT_TABLE_NAME
            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_ID_COLUMN
            + " = " + ORDER_PRODUCT_TABLE_NAME + "." + ORDER_PRODUCT_PRODUCT_ID_COLUMN
            + " INNER JOIN " + STOCK_TABLE_NAME
            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_STOCK_ID_COLUMN
            + " = " + STOCK_TABLE_NAME + "." + STOCK_ID_COLUMN
            + " RIGHT JOIN " + CLIENTS_TABLE_NAME
            + " ON " + CLIENTS_TABLE_NAME + "." + CLIENT_ID_COLUMN
            + " = " + ORDER_TABLE_NAME + "." + ORDER_CLIENT_ID_COLUMN
            + " WHERE "
            + CLIENTS_TABLE_NAME + "." + CLIENT_TOKEN_COLUMN
            + " = ?";

    static final String GET_CLIENT_INFO = "SELECT * FROM " + CLIENTS_TABLE_NAME
            + " WHERE " + CLIENT_EMAIL_COLUMN + " = ? AND "
            + CLIENT_PASSWORD_COLUMN + " = ?";


    static final String GET_ORDERS = "SELECT * FROM " + PRODUCT_TABLE_NAME
            + " INNER JOIN " + STOCK_TABLE_NAME
            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_STOCK_ID_COLUMN
            + " = " + STOCK_TABLE_NAME + "." + STOCK_ID_COLUMN
            + " INNER JOIN " + ORDER_PRODUCT_TABLE_NAME
            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_ID_COLUMN
            + " = " + ORDER_PRODUCT_TABLE_NAME + "." + ORDER_PRODUCT_PRODUCT_ID_COLUMN
            + " INNER JOIN " + ORDER_TABLE_NAME
            + " ON " + ORDER_TABLE_NAME + "." + ORDER_ID_COLUMN
            + " = " + ORDER_PRODUCT_TABLE_NAME + "." + ORDER_PRODUCT_ORDER_ID_COLUMN
            + " LEFT JOIN " + EMPLOYEES_TABLE_NAME
            + " ON " + ORDER_TABLE_NAME + "." + ORDER_EMPLOYEE_ID_COLUMN
            + " = " + EMPLOYEES_TABLE_NAME + "." + EMPLOYEE_ID_COLUMN
            + " RIGHT JOIN " + STATUS_TABLE_NAME
            + " ON " + ORDER_TABLE_NAME + "." + ORDER_STATUS_ID_COLUMN
            + " = " + STATUS_TABLE_NAME + "." + STATUS_ID_COLUMN;

    static final String ADD_STATUS = "insert into " + STATUS_TABLE_NAME
            + " (" + STATUS_DESCRIPTION_COLUMN + ")"
            + " values (?)";

    static final String UPDATE_STATUS = "update " + STATUS_TABLE_NAME
            + " set " + STATUS_DESCRIPTION_COLUMN + " = ? "
            + " where " + STATUS_ID_COLUMN + " = ?";

    static final String REMOVE_STATUS = "delete from " + STATUS_TABLE_NAME
            + " where " + STATUS_ID_COLUMN + " = ?";

    static final String GET_STOCKS = "select * from " + STOCK_TABLE_NAME
            + " left join " + PRODUCT_TABLE_NAME
            + " on " + PRODUCT_TABLE_NAME + "." + PRODUCT_STOCK_ID_COLUMN
            + " = " + STOCK_TABLE_NAME + "." + STOCK_ID_COLUMN;

    static final String ADD_STOCK = "insert into " + STOCK_TABLE_NAME
            + " (" + STOCK_ADDRESS_COLUMN + ")"
            + "values (?)";

    static final String UPDATE_STOCK = "update " + STOCK_TABLE_NAME
            + " set " + STOCK_ADDRESS_COLUMN + " = ? "
            + " where " + STOCK_ID_COLUMN + " = ?";

    static final String REMOVE_STOCK = "delete from " + STOCK_TABLE_NAME
            + " where " + STOCK_ID_COLUMN + " = ?";

    static final String ADD_ORDER = "insert into " + ORDER_TABLE_NAME
            + "(" + ORDER_DATE_COLUMN + ","
            + ORDER_STATUS_ID_COLUMN + ","
            + ORDER_CLIENT_ID_COLUMN + ","
            + ORDER_PHONE_NUMBER_COLUMN + ","
            + ORDER_ADDRESS_COLUMN + ","
            + ORDER_CLIENT_NAME_COLUMN + ","
            + ORDER_SUM_COLUMN + ") "
            + "VALUES (?,?,?,?,?,?,?)";

    static final String BIND_PRODUCT = "insert into " + ORDER_PRODUCT_TABLE_NAME
            + " (" + ORDER_PRODUCT_ORDER_ID_COLUMN
            + "," + ORDER_PRODUCT_PRODUCT_ID_COLUMN
            + "," + ORDER_PRODUCT_AMOUNT_COLUMN + ")"
            + " values (?,?,?)";

    static final String REMOVE_ORDER = "delete from " + ORDER_TABLE_NAME
            + " where " + ORDER_ID_COLUMN + " = ?";

    static final String GET_ORDER_PRODUCTS = "select "
            + ORDER_PRODUCT_PRODUCT_ID_COLUMN + ","
            + ORDER_PRODUCT_AMOUNT_COLUMN + " from "
            + ORDER_PRODUCT_TABLE_NAME + " where "
            + ORDER_PRODUCT_ORDER_ID_COLUMN + " = ?";

    static final String RESET_STOCK_AMOUNT = "update " + PRODUCT_TABLE_NAME
            + "set " + PRODUCT_STOCK_AMOUNT_COLUMN
            + " = " + PRODUCT_STOCK_AMOUNT_COLUMN
            + " + ?"
            + " where " + PRODUCT_ID_COLUMN
            + " = ?";

    static final String REMOVE_FROM_STOCK = "update " + PRODUCT_TABLE_NAME
            + "set " + PRODUCT_STOCK_AMOUNT_COLUMN
            + " = " + PRODUCT_STOCK_AMOUNT_COLUMN
            + " - ?"
            + " where " + PRODUCT_ID_COLUMN
            + " = ?";

    static final String ADD_PRODUCT = "insert into " + PRODUCT_TABLE_NAME
            + "(" + PRODUCT_TITLE_COLUMN + ","
            + "(" + PRODUCT_FEATURES_COLUMN + ","
            + "(" + PRODUCT_STOCK_AMOUNT_COLUMN + ","
            + "(" + PRODUCT_PRICE_COLUMN + ","
            + "(" + PRODUCT_METRICS_COLUMN + ","
            + "(" + PRODUCT_IMAGE_URL + ","
            + "(" + PRODUCT_DESCRIPTION_COLUMN + ","
            + "(" + PRODUCT_ARTICUL_COLUMN + ","
            + "(" + PRODUCT_CATEGORY_ID_COLUMN + ","
            + "(" + PRODUCT_STOCK_ID_COLUMN + ") "
            + "VALUES (?,?,?,?,?,?,?,?,?,?)";

    static final String REMOVE_PRODUCT = "delete from " + PRODUCT_TABLE_NAME
            + " where " + PRODUCT_ID_COLUMN + " = ?";

    static final String GET_EMPLOYEES = "select * from " + ORDER_TABLE_NAME
            + "inner join " + STATUS_TABLE_NAME
            + " on " + STATUS_TABLE_NAME + "." + STATUS_ID_COLUMN
            + " = " + ORDER_TABLE_NAME + "." + ORDER_STATUS_ID_COLUMN
            + " right join " + EMPLOYEES_TABLE_NAME
            + " on " + EMPLOYEES_TABLE_NAME + "." + EMPLOYEE_ID_COLUMN
            + " = " + ORDER_TABLE_NAME + "." + ORDER_EMPLOYEE_ID_COLUMN;

    static final String GET_EMPLOYEE_INFO = "select * from " + EMPLOYEES_TABLE_NAME
            + " where " + EMPLOYEE_EMAIL_COLUMN + " = ?";

    static final String BIND_EMPLOYEE = "update " + ORDER_TABLE_NAME
            + " set " + ORDER_EMPLOYEE_ID_COLUMN + " = ?"
            + " , " + ORDER_STATUS_ID_COLUMN + " = ?"
            + " where " + ORDER_ID_COLUMN + " = ?";

    static final String CHANGE_ORDER_STATUS = "update " + ORDER_TABLE_NAME
            + " set " + ORDER_STATUS_ID_COLUMN + " = ?";

}
