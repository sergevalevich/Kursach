package com.valevich.kursach.model.request.product;

import com.valevich.kursach.model.request.AccessPayload;
import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class NewProductPayload extends AccessPayload implements Validatable{
    private String title;
    private String features;
    private int amount;
    private double price;
    private String metrics;
    private String imageUrl;
    private String description;
    private String articul;
    private int categoryId;
    private int stockId;

    @Override
    public boolean isValid() {
        return title != null && !title.isEmpty()
                && categoryId != 0 && stockId != 0
                && amount != 0 && price != 0
                && metrics != null && !metrics.isEmpty()
                && articul != null && !articul.isEmpty()
                && super.isValid();
    }
}
