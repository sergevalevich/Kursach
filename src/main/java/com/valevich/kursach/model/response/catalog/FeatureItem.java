package com.valevich.kursach.model.response.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FeatureItem {
    private String name;
    private String value;
}
