package com.valevich.kursach.model.request.client;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class AddClientPayload implements Validatable {

    private String name;
    private String surname;
    private String phoneNumber;
    private String address;
    private String email;
    private String password;
    private String employeeToken;

    @Override
    public boolean isValid() {
        return employeeToken != null && !employeeToken.isEmpty()
                && email != null && !email.isEmpty()
                && password != null && !password.isEmpty();
    }
}
