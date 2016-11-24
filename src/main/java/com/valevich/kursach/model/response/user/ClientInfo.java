package com.valevich.kursach.model.response.user;

import com.valevich.kursach.model.response.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
    private List<OrderItem> orders;

    public void addOrder(OrderItem order) {
        if(orders == null) orders = new ArrayList<>();
        orders.add(order);
    }
}
