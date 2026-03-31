package com.example.dessert.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String customerName;
    private int quantity;
}
