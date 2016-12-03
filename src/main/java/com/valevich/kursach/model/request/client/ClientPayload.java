package com.valevich.kursach.model.request.client;

import com.valevich.kursach.model.request.AccessPayload;
import lombok.Data;

@Data
public class ClientPayload extends AccessPayload {
    private String name;
    private String surname;
    private String phoneNumber;
    private String address;
    private String email;
    private String password;
}
