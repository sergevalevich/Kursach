package com.valevich.kursach.model.response.employee;

import com.valevich.kursach.model.response.order.OrderItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeeItem {
    private int id;
    private String name;
    private int age;
    private String position;
    private String phoneNumber;
    private String surname;
    private String email;

    private List<OrderItem> orders;
    public void addOrder(OrderItem item) {
        if(orders == null) orders = new ArrayList<>();
        orders.add(item);
    }
}
