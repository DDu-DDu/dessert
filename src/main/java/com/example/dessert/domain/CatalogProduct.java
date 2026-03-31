package com.example.dessert.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogProduct {
    private Long id;
    private String name;
    private String shortDescription;
    private String category;
    private String badge;
    private String imageType;
}
