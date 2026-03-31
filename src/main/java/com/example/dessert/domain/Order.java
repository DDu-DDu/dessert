package com.example.dessert.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Long productId;
    private String customerName;
    private int quantity;
    private OrderStatus status;
    private LocalDateTime orderedAt;
}
