package com.valevich.kursach.model.request.stock;

import com.valevich.kursach.model.request.AccessPayload;
import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class NewStockPayload extends AccessPayload implements Validatable {

    private String address;

    @Override
    public boolean isValid() {
        return address != null && !address.isEmpty() && super.isValid();
    }
}
