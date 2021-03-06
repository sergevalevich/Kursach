package com.valevich.kursach.model.request.client;

import com.valevich.kursach.model.request.AccessPayload;
import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

@Data
public class DeleteClientPayload extends AccessPayload implements Validatable {
    private int id;
}
