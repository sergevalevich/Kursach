package com.valevich.kursach.model.request.category;


import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class NewCategoryPayload implements Validatable {

    private String name;

    private String token;

    @Override
    public boolean isValid() {
        return name != null && !name.isEmpty() && token != null && !token.isEmpty();
    }
}
