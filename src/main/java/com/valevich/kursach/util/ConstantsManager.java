package com.valevich.kursach.util;

public interface ConstantsManager {
    String NO_ROWS_AFFECTED = "NO ROWS AFFECTED";
    String NO_ID_OBTAINED = "CREATION FAILED. NO ID OBTAINED";
    String INVALID_REQUEST = "INVALID REQUEST";
    String ACCESS_NOT_ALLOWED = "ACCESS NOT ALLOWED";
    String OPERATION_SUCCESSFULL = "OPERATION PASSED ";
    String INVALID_REQUEST_FRIENDLY = "WRONG CREDENTIALS";
    String DUPLICATE_EMAIL = "Пользователь с таким почтовым адресом уже зарегистрирован";
    String WRONG_CREDENTIALS = "Неправильный email или пароль";

    String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
}
