package com.valevich.kursach.model.request.stock;

import com.valevich.kursach.model.request.AccessPayload;
import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class DeleteStockPayload extends AccessPayload implements Validatable{
    private int id;
}
