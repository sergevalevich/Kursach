package com.valevich.kursach.model.request.order;

import com.valevich.kursach.model.request.AccessPayload;
import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class SetOrderStatusPayload extends AccessPayload implements Validatable{
    private int orderStatusId;

    @Override
    public boolean isValid() {
        return orderStatusId != 0 && super.isValid();
    }
}
