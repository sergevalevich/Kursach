package com.valevich.kursach.model.request.client;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class DeleteClientPayload implements Validatable{
    private int id;
    private String token;

    @Override
    public boolean isValid() {
        return token != null && !token.isEmpty();
    }
}
