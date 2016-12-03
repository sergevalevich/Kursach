package com.valevich.kursach.model.response.status;

import com.valevich.kursach.model.response.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatusItem {
    private int id;
    private String description;
    private List<OrderItem> orders;

    public void addOrder(OrderItem order) {
        if(orders == null) orders = new ArrayList<>();
        orders.add(order);
    }
}
