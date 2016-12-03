package com.valevich.kursach.model.request.client;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class AddClientPayload extends ClientPayload implements Validatable {

    @Override
    public boolean isValid() {
        return getEmail() != null && !getEmail().isEmpty()
                && getEmail() != null && !getPassword().isEmpty()
                && super.isValid();
    }
}
