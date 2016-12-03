package com.valevich.kursach.model.request.stock;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class UpdateStockPayload extends NewStockPayload implements Validatable {
    private int id;
}
