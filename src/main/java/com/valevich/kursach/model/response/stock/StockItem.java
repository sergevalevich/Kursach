package com.valevich.kursach.model.response.stock;

import com.valevich.kursach.model.response.catalog.ProductItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class StockItem {
    private int id;
    private String address;
    private List<ProductItem> products;

    public void addProduct(ProductItem productItem) {
        if (products == null) products = new ArrayList<>();
        products.add(productItem);
    }
}
