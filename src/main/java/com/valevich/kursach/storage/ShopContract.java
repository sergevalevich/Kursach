package com.valevich.kursach.storage;

import static javafx.scene.input.KeyCode.O;

public class ShopContract {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/materik";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "25041997sv";

    // category table
    static final String CATEGORY_TABLE_NAME = "category";
    static final String CATEGORY_NAME_COLUMN = "name";
    static final String CATEGORY_ID_COLUMN = "id";
    //client table
    private static final String CLIENTS_TABLE_NAME = "client";
    static final String CLIENT_ID_COLUMN = "id";
    static final String CLIENT_TOKEN_COLUMN = "token";
    static final String CLIENT_EMAIL_COLUMN = "email";
    static final String CLIENT_PASSWORD_COLUMN = "password";
    static final String CLIENT_ADDRESS_COLUMN = "address";
    static final String CLIENT_PHONE_COLUMN = "phone_number";
    static final String CLIENT_NAME_COLUMN = "name";
    static final String CLIENT_SURNAME_COLUMN = "surname";
    static final String CLIENT_CARD_NUMBER = "card_number";
    static final String CLIENT_CARD_CVV = "card_cvv";
    static final String CLIENT_CARD_OWNER = "card_owner";
    static final String CLIENT_CARD_EXP_DATE = "card_exp_date";
    //employee table
    private static final String EMPLOYEES_TABLE_NAME = "employee";
    private static final String EMPLOYEE_TOKEN_COLUMN = "token";
    //product table
    private static final String PRODUCT_TABLE_NAME = "product";
    static final String PRODUCT_TITLE_COLUMN = "title";
    static final String PRODUCT_ID_COLUMN = "id";
    static final String PRODUCT_FEATURES_COLUMN = "features";
    static final String PRODUCT_SUPPLY_PERIOD_COLUMN = "supply_period";
    static final String PRODUCT_STOCK_AMOUNT_COLUMN = "stock_amount";
    static final String PRODUCT_PRICE_COLUMN = "price";
    static final String PRODUCT_METRICS_COLUMN = "metrics";
    static final String PRODUCT_IMAGE_URL = "imageUrl";
    static final String PRODUCT_FIRST_SUPPLY_DATE_COLUMN = "first_supply_date";
    static final String PRODUCT_DESCRIPTION_COLUMN = "description";
    static final String PRODUCT_ARTICUL_COLUMN = "articul";
    static final String PRODUCT_CATEGORY_ID_COLUMN = "categoryId";
    static final String PRODUCT_STOCK_ID_COLUMN = "stockId";
    static final String PRODUCT_PROVIDER_ID_COLUMN = "providerId";
    //order table
    static final String ORDER_TABLE_NAME = "`order`";
    static final String ORDER_ID_COLUMN = "id";
    static final String ORDER_DATE_COLUMN = "date";
    static final String ORDER_CLIENT_ID_COLUMN = "client_id";
    static final String ORDER_STATUS_ID_COLUMN = "status_id";
    static final String ORDER_EMPLOYEE_ID_COLUMN = "employee_id";
    static final String ORDER_PHONE_NUMBER_COLUMN = "phone_number";
    static final String ORDER_ADDRESS_COLUMN = "address";
    static final String ORDER_CLIENT_NAME_COLUMNN = "client_name";
    static final String ORDER_SUM_COLUMN = "sum";
    //orderproduct table
    static final String ORDER_PRODUCT_TABLE_NAME = "orderproduct";
    static final String ORDER_PRODUCT_ORDER_ID_COLUMN = "orderId";
    static final String ORDER_PRODUCT_PRODUCT_ID_COLUMN = "productId";
    //privilege table
    private static final String PRIVILEGE_TABLE_NAME = "privilege";


    static final String ADD_CATEGORY = "INSERT INTO "
            + CATEGORY_TABLE_NAME
            + " (" + CATEGORY_NAME_COLUMN + ")"
            + " VALUES"
            + "(?)";

    static final String UPDATE_CATEGORY = "UPDATE "
            + CATEGORY_TABLE_NAME
            + " SET " + CATEGORY_NAME_COLUMN + " = ?"
            + " WHERE " + CATEGORY_ID_COLUMN + " = ?";

    static final String REMOVE_CATEGORY = "DELETE FROM "
            + CATEGORY_TABLE_NAME
            + " WHERE " + CATEGORY_ID_COLUMN + " = ?";

    static final String GET_ALL_CATEGORIES = "SELECT * FROM " + CATEGORY_TABLE_NAME;

    static final String GET_CATALOG = "SELECT * FROM " + PRODUCT_TABLE_NAME
            + " RIGHT JOIN " + CATEGORY_TABLE_NAME
            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_CATEGORY_ID_COLUMN
            + " = " + CATEGORY_TABLE_NAME + "." + CATEGORY_ID_COLUMN
            + " ORDER BY " + PRODUCT_CATEGORY_ID_COLUMN;

    static final String IS_ACCESS_ALLOWED = "SELECT priv & ? FROM "
            + EMPLOYEES_TABLE_NAME
            + " WHERE token = ?";

    static final String GET_ALL_TOKENS = "SELECT " + CLIENT_TOKEN_COLUMN
            + " FROM " + CLIENTS_TABLE_NAME
            + " WHERE " + CLIENT_TOKEN_COLUMN + " = ?"
            + " UNION"
            + " SELECT " + EMPLOYEE_TOKEN_COLUMN
            + " FROM " + EMPLOYEES_TABLE_NAME
            + " WHERE " + EMPLOYEE_TOKEN_COLUMN + " = ?";

    static final String GET_ALL_PRIVILEGES = "SELECT * FROM " + PRIVILEGE_TABLE_NAME;

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
            +  "," + CLIENT_TOKEN_COLUMN + ")"
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
            +  " WHERE " + CLIENT_ID_COLUMN + " =?";

    static final String UPDATE_CLIENT_CLIENT = "UPDATE "
            + CLIENTS_TABLE_NAME
            + " SET " + CLIENT_NAME_COLUMN + " =?"
            + "," + CLIENT_SURNAME_COLUMN + " =?"
            + "," + CLIENT_PHONE_COLUMN + " =?"
            + "," + CLIENT_ADDRESS_COLUMN + " =?"
            + "," + CLIENT_EMAIL_COLUMN + " =?"
            + "," + CLIENT_PASSWORD_COLUMN + " =?"
            +  " WHERE " + CLIENT_TOKEN_COLUMN + " =?";

    static final String DELETE_CLIENT = "DELETE FROM " + CLIENTS_TABLE_NAME
            + " WHERE " + CLIENT_ID_COLUMN + " =?";

    static final String GET_CLIENT = "SELECT *"
            + " FROM " + CLIENTS_TABLE_NAME
            + " WHERE " + CLIENT_EMAIL_COLUMN + " = ? AND " + CLIENT_PASSWORD_COLUMN + " = ?";

    static final String GET_ALL_CLIENTS_WITH_ORDERS = "SELECT * FROM " + PRODUCT_TABLE_NAME
            + " INNER JOIN " + ORDER_PRODUCT_TABLE_NAME
            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_ID_COLUMN
            + " = " + ORDER_PRODUCT_TABLE_NAME + "." + ORDER_PRODUCT_PRODUCT_ID_COLUMN
            + " INNER JOIN " + ORDER_TABLE_NAME
            + " ON " + ORDER_TABLE_NAME + "." + ORDER_ID_COLUMN
            + " = " + ORDER_PRODUCT_TABLE_NAME + "." + ORDER_PRODUCT_ORDER_ID_COLUMN
            + " RIGHT JOIN " + CLIENTS_TABLE_NAME
            + " ON " + CLIENTS_TABLE_NAME + "." + CLIENT_ID_COLUMN
            + " = " + ORDER_TABLE_NAME + "." + ORDER_CLIENT_ID_COLUMN;


    public interface RolesContract {
        int EDIT_CATALOG = 1;
        int READ_CLIENTS = 2;
        int EDIT_CLIENTS = 4;
    }

}
