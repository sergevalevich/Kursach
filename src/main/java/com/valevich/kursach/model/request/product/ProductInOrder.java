package com.valevich.kursach.model.request.product;

import lombok.Data;

@Data
public class ProductInOrder {
    private int id;
    private int amount;
    private double price;
}
