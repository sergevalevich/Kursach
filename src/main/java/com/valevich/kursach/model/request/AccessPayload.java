package com.valevich.kursach.model.request;

import lombok.Data;

@Data
public class AccessPayload implements Validatable{
    private String userEmail;
    private String userPassword;

    @Override
    public boolean isValid() {
        return userEmail != null && !userEmail.isEmpty()
                && userPassword != null && !userPassword.isEmpty();
    }
}
