package com.valevich.kursach.model.response.catalog;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CatalogItem {
    private int id;
    private String name;
    private String imageUrl;
    private List<ProductItem> products;

    public void addProduct(ProductItem product) {
        if(products == null) products = new ArrayList<>();
        products.add(product);
    }
}
