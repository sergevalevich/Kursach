package com.valevich.kursach.model.request.client;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class UpdateClientPayload extends AddClientPayload implements Validatable {

    private int id;

    @Override
    public boolean isValid() {
        return getEmployeeToken() != null && !getEmployeeToken().isEmpty()
                && getEmail() != null && !getEmail().isEmpty()
                && getPassword() != null && !getPassword().isEmpty();
    }
}
