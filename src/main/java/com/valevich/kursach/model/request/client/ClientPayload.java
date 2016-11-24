package com.valevich.kursach.model.request.client;

import lombok.Data;

@Data
public class ClientPayload {
    private String name;
    private String surname;
    private String phoneNumber;
    private String address;
    private String email;
    private String password;
}
