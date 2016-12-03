package com.valevich.kursach.model.request.order;

import com.valevich.kursach.model.request.AccessPayload;
import lombok.Data;

@Data
public class DeleteOrderPayload extends AccessPayload {
    private int id;
}
