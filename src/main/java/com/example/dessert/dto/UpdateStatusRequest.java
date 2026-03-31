package com.example.dessert.dto;

import com.example.dessert.domain.CustomsStatus;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    private CustomsStatus customsStatus;
}
