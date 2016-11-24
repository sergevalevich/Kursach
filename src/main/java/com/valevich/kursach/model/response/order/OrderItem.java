package com.valevich.kursach.model.response.order;

import com.valevich.kursach.model.response.catalog.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItem {
    private int id;
    private Date date;
    private String clientFullName;
    private String clientPhone;
    private String status;
    private String employeeName;
    private String employeeSurname;
    private String employeePhone;
    private String deliveryAddress;
    private double sum;
    private List<ProductItem> products;

    public void addProduct(ProductItem item) {
        if(products == null) products = new ArrayList<>();
        products.add(item);
    }
}
