package com.valevich.kursach.model.request.category;


import com.valevich.kursach.model.request.AccessPayload;
import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class NewCategoryPayload extends AccessPayload implements Validatable {

    private String name;

    @Override
    public boolean isValid() {
        return name != null && !name.isEmpty() && super.isValid();
    }
}
