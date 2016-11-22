package com.valevich.kursach.model.request.category;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class DeleteCategoryPayload implements Validatable {
    private String token;
    private int id;

    @Override
    public boolean isValid() {
        return token != null && !token.isEmpty();
    }
}
