package com.valevich.kursach.model.request.order;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class BindEmployeePayload extends SetOrderStatusPayload implements Validatable {

    private int employeeId;

    @Override
    public boolean isValid() {
        return employeeId != 0 && super.isValid();
    }
}
