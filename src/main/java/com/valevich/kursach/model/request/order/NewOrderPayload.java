package com.valevich.kursach.model.request.order;

import com.valevich.kursach.model.request.Validatable;
import com.valevich.kursach.model.request.product.ProductInOrder;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class NewOrderPayload implements Validatable {
    private Date date;
    private String clientFullName;
    private String clientPhone;
    private String deliveryAddress;
    private int clientId;
    private List<ProductInOrder> products;
    private String token;

    @Override
    public boolean isValid() {
        return token != null && !token.isEmpty()
                && date != null
                && clientFullName != null && !clientFullName.isEmpty()
                && clientPhone != null && !clientPhone.isEmpty()
                && deliveryAddress != null && !deliveryAddress.isEmpty()
                && clientId != 0
                && products != null && !products.isEmpty();
    }

    public double getSum() {
        double sum = 0;
        for(ProductInOrder product:products) {
            sum+=product.getPrice()*product.getAmount();
        }
        return sum;
    }
}
