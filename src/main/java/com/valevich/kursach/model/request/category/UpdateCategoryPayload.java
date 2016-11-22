package com.valevich.kursach.model.request.category;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class UpdateCategoryPayload implements Validatable {
    private int id;
    private String name;
    private String token;

    @Override
    public boolean isValid() {
        return token != null && !token.isEmpty() && name != null && !name.isEmpty();
    }
}
