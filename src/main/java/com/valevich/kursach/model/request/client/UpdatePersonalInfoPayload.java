package com.valevich.kursach.model.request.client;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class UpdatePersonalInfoPayload extends ClientPayload implements Validatable {

    private String token;

    @Override
    public boolean isValid() {
        return token != null && !token.isEmpty()
                && getEmail() != null && !getEmail().isEmpty()
                && getPassword() != null && !getPassword().isEmpty();
    }
}
