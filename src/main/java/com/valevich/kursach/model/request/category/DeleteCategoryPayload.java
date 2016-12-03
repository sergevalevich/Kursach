package com.valevich.kursach.model.request.category;

import com.valevich.kursach.model.request.AccessPayload;
import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class DeleteCategoryPayload extends AccessPayload implements Validatable {
    private int id;
}
