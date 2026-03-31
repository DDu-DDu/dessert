package com.example.dessert.service;

import com.example.dessert.domain.CatalogProduct;
import com.example.dessert.domain.Notification;
import com.example.dessert.domain.Order;
import com.example.dessert.domain.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class MockDataService {
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private Product product;
    private final List<CatalogProduct> catalogProducts = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final List<Notification> notifications = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            product = readProduct("mock/products.json");
            catalogProducts.addAll(readList("mock/catalog-products.json", new TypeReference<List<CatalogProduct>>() {}));
            orders.addAll(readList("mock/orders.json", new TypeReference<List<Order>>() {}));
            notifications.addAll(readList("mock/notifications.json", new TypeReference<List<Notification>>() {}));
        } catch (Exception e) {
            throw new IllegalStateException("Mock data loading failed", e);
        }
    }

    private Product readProduct(String path) throws Exception {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return objectMapper.readValue(is, Product.class);
        }
    }

    private <T> List<T> readList(String path, TypeReference<List<T>> typeReference) throws Exception {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return objectMapper.readValue(is, typeReference);
        }
    }
}
