package com.valevich.kursach.model.response.catalog;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ProductItem {
    private int id;
    private int stockId;
    private int providerId;
    private String name;
    private String features;
    private int supplyPeriod;
    private int stockAmount;
    private double price;
    private String metrics;
    private String imageUrl;
    private Date firstSupplyDate;
    private String description;
    private String articul;
}
