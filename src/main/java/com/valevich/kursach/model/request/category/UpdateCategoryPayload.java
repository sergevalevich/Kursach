package com.valevich.kursach.model.request.category;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class UpdateCategoryPayload extends NewCategoryPayload implements Validatable {
    private int id;
}
