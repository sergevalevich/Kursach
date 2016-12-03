package com.valevich.kursach.model.request.status;

import com.valevich.kursach.model.request.AccessPayload;
import lombok.Data;

@Data
public class DeleteStatusPayload extends AccessPayload {
    private int id;
}
