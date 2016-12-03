package com.valevich.kursach.model.request.product;

import com.valevich.kursach.model.request.AccessPayload;
import lombok.Data;

@Data
public class UnbindProductPayload extends AccessPayload {
    private int orderId;
    private int productId;
}
