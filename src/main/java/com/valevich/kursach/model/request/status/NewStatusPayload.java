package com.valevich.kursach.model.request.status;

import com.valevich.kursach.model.request.AccessPayload;
import lombok.Data;

@Data
public class NewStatusPayload extends AccessPayload {
    private String description;

    @Override
    public boolean isValid() {
        return description != null && !description.isEmpty() && super.isValid();
    }
}
