package com.valevich.kursach.model.request.status;

import lombok.Data;

@Data
public class UpdateStatusPayload extends NewStatusPayload {
    private int id;
}
