package com.example.dessert.service;

import com.example.dessert.domain.Notification;
import com.example.dessert.domain.Order;
import com.example.dessert.domain.OrderStatus;
import com.example.dessert.dto.CreateOrderRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class OrderService {

    private final MockDataService mockDataService;

    public OrderService(MockDataService mockDataService) {
        this.mockDataService = mockDataService;
    }

    public List<Order> getOrders() {
        return mockDataService.getOrders()
                .stream()
                .sorted(Comparator.comparing(Order::getOrderedAt))
                .toList();
    }

    public List<Notification> getNotifications() {
        return mockDataService.getNotifications()
                .stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .toList();
    }

    public void createOrder(CreateOrderRequest request) {
        long nextId = mockDataService.getOrders().stream()
                .mapToLong(Order::getId)
                .max()
                .orElse(100L) + 1;

        Order order = Order.builder()
                .id(nextId)
                .productId(mockDataService.getProduct().getId())
                .customerName(request.getCustomerName())
                .quantity(request.getQuantity())
                .status(OrderStatus.PENDING)
                .orderedAt(LocalDateTime.now())
                .build();

        mockDataService.getOrders().add(order);
        addNotification(order.getId(), request.getCustomerName() + "님의 선주문이 접수되었습니다.");
    }

    public void cancelOrder(Long orderId) {
        mockDataService.getOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .ifPresent(order -> {
                    order.setStatus(OrderStatus.CANCELLED);
                    addNotification(order.getId(), order.getCustomerName() + " 주문이 취소되었습니다.");
                });
    }

    public void addNotification(Long orderId, String message) {
        long nextId = mockDataService.getNotifications().stream()
                .mapToLong(Notification::getId)
                .max()
                .orElse(0L) + 1;

        mockDataService.getNotifications().add(Notification.builder()
                .id(nextId)
                .orderId(orderId)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build());
    }
}
