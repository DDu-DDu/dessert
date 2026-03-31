package com.example.dessert.service;

import com.example.dessert.domain.CatalogProduct;
import com.example.dessert.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {

    private final MockDataService mockDataService;

    public CatalogService(MockDataService mockDataService) {
        this.mockDataService = mockDataService;
    }

    public List<CatalogProduct> getCatalogProducts() {
        return mockDataService.getCatalogProducts();
    }

    public Product getFeaturedProduct() {
        return mockDataService.getProduct();
    }
}
