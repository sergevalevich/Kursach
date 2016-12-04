package com.valevich.kursach.storage;

public class ShopContract {

    private static String INSERTED_ID = "@inserted_id";
    static final String GET_INSERTED_ID = "select" + INSERTED_ID;

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
    static final String EMPLOYEES_TABLE_NAME = "employee";
    static final String EMPLOYEE_TOKEN_COLUMN = "token";
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
    static final String ORDER_PRODUCT_TABLE_NAME = "orderproduct";
    static final String ORDER_PRODUCT_ORDER_ID_COLUMN = "orderId";
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
    //privilege table
    private static final String PRIVILEGE_TABLE_NAME = "privilege";


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

//    static final String IS_ACCESS_ALLOWED = "SELECT " + EMPLOYEE_PRIV_COLUMN + " & ? FROM "
//            + EMPLOYEES_TABLE_NAME
//            + " WHERE token = ?";

    static final String GET_ALL_TOKENS = "SELECT " + CLIENT_TOKEN_COLUMN
            + " FROM " + CLIENTS_TABLE_NAME
            + " WHERE " + CLIENT_TOKEN_COLUMN + " = ?"
            + " UNION"
            + " SELECT " + EMPLOYEE_TOKEN_COLUMN
            + " FROM " + EMPLOYEES_TABLE_NAME
            + " WHERE " + EMPLOYEE_TOKEN_COLUMN + " = ?";

//    static final String GRANT_PROCEDURE = "GRANT EXECUTE ON PROCEDURE =? TO 'lalala@mail.ru';";

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


    static final String GET_CLIENT_INFO =
            "call materik.client_info(?, ?)";
//            "SELECT * FROM " + PRODUCT_TABLE_NAME
//            + " INNER JOIN " + STOCK_TABLE_NAME
//            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_STOCK_ID_COLUMN
//            + " = " + STOCK_TABLE_NAME + "." + STOCK_ID_COLUMN
//            + " INNER JOIN " + ORDER_PRODUCT_TABLE_NAME
//            + " ON " + PRODUCT_TABLE_NAME + "." + PRODUCT_ID_COLUMN
//            + " = " + ORDER_PRODUCT_TABLE_NAME + "." + ORDER_PRODUCT_PRODUCT_ID_COLUMN
//            + " INNER JOIN " + ORDER_TABLE_NAME
//            + " ON " + ORDER_TABLE_NAME + "." + ORDER_ID_COLUMN
//            + " = " + ORDER_PRODUCT_TABLE_NAME + "." + ORDER_PRODUCT_ORDER_ID_COLUMN
//            + " INNER JOIN " + STATUS_TABLE_NAME
//            + " ON " + ORDER_TABLE_NAME + "." + ORDER_STATUS_ID_COLUMN
//            + " = " + STATUS_TABLE_NAME + "." + STATUS_ID_COLUMN
//            + " LEFT JOIN " + EMPLOYEES_TABLE_NAME
//            + " ON " + ORDER_TABLE_NAME + "." + ORDER_EMPLOYEE_ID_COLUMN
//            + " = " + EMPLOYEES_TABLE_NAME + "." + EMPLOYEE_ID_COLUMN
//            + " INNER JOIN " + CLIENTS_TABLE_NAME
//            + " ON " + CLIENTS_TABLE_NAME + "." + CLIENT_ID_COLUMN
//            + " = " + ORDER_TABLE_NAME + "." + ORDER_CLIENT_ID_COLUMN
//            + " WHERE "
//            + CLIENTS_TABLE_NAME + "." + CLIENT_EMAIL_COLUMN
//            + " = ? AND "
//            + CLIENTS_TABLE_NAME + "." + CLIENT_PASSWORD_COLUMN + " = ?";

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

    static final String ADD_STATUS = "call materik.add_status(?," + INSERTED_ID + ")";

    static final String UPDATE_STATUS = "call materik.update_status(?,?)";

    static final String REMOVE_STATUS = "call materik.remove_status(?)";

    static final String GET_STOCKS = "call materik.get_stocks()";

    static final String ADD_STOCK = "call materik.add_stock(?," + INSERTED_ID + ")";

    static final String UPDATE_STOCK = "call materik.update_stock(?,?)";

    static final String REMOVE_STOCK = "call materik.remove_stock(?)";

    static final String ADD_ORDER = "call materik.add_order(?,?,?,?,?,?,?,"+ INSERTED_ID +")";

    static final String BIND_PRODUCT = "call materik.bind_product(?,?,?,"+ INSERTED_ID +")";

    static final String UNBIND_PRODUCT = "call materik.unbind_product(?,?)";

    static final String REMOVE_ORDER = "call materik.remove_order(?)";

    static final String GET_ORDER_PRODUCTS = "call materik.get_order_products(?)";

    static final String RESET_STOCK_AMOUNT = "call materik.reset_stock_amount(?,?)";

    static final String REMOVE_FROM_STOCK = "call materik.remove_from_stock(?,?)";

    static final String ADD_PRODUCT = "call materik.add_product(?,?,?,?,?,?,?,?,?,?,"+ INSERTED_ID +")";

    static final String REMOVE_PRODUCT = "call materik.remove_product(?)";

    static final String GET_EMPLOYEES = "call materik.get_employees()";

    static final String GET_EMPLOYEE_INFO = "call materik.get_employee_info(?)";

    static final String BIND_EMPLOYEE = "call materik.bind_employee(?,?)";

    static final String CHANGE_ORDER_STATUS = "call materik.set_order_status(?)";

//    public interface RolesContract {
//        int EDIT_CATALOG = 1;
//        int READ_CLIENTS = 2;
//        int EDIT_CLIENTS = 4;
//        int READ_ORDERS = 8;
//        int EDIT_ORDERS = 16;
//    }

}
