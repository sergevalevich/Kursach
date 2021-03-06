package com.valevich.kursach.model.response.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class ProductItem {
    private int id;
    private String stockAddress;
    private String name;
    private String features;
    private int stockAmount;
    private double price;
    private String metrics;
    private String imageUrl;
    private String description;
    private String articul;
}
