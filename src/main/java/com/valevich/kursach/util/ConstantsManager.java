package com.valevich.kursach.util;

public interface ConstantsManager {
    String NO_ROWS_AFFECTED = "NO ROWS AFFECTED";
    String NO_ID_OBTAINED = "CREATION FAILED. NO ID OBTAINED";
    String INVALID_REQUEST = "INVALID REQUEST";
    String ACCESS_NOT_ALLOWED = "ACCESS NOT ALLOWED";
    String OPERATION_SUCCESSFULL = "OPERATION PASSED ";
    String DUPLICATE_EMAIL = "Пользователь с таким почтовым адресом уже зарегистрирован";
    String WRONG_CREDENTIALS = "Неправильный Email или пароль";

    int DEFAULT_ORDER_STATUS = 3;

    String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    String DB_URL = "jdbc:mysql://localhost/materik";

    //  Database credentials
    String ROOT_USER = "root";
    String ROOT_PASS = "25041997sv";
}
