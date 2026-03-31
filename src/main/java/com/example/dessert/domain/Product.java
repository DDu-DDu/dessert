package com.example.dessert.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String description;
    private int expectedStock;
    private int securedStock;
    private CustomsStatus customsStatus;
    private String origin;
    private String flavorNote;
}
