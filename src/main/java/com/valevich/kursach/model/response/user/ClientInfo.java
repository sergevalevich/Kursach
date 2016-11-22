package com.valevich.kursach.model.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@AllArgsConstructor
@Data
public class ClientInfo {
    private int id;
    private String name;
    private String surname;
    private String phoneNumber;
    private double cardNumber;
    private int cardCvv;
    private String cardOwner;
    private Date cardExpDate;
    private String address;
    private String email;
    private String token;
}
