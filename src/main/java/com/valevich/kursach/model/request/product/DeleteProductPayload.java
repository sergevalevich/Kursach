package com.valevich.kursach.model.request.product;

import com.valevich.kursach.model.request.AccessPayload;
import lombok.Data;

@Data
public class DeleteProductPayload extends AccessPayload{
    private int id;
}
