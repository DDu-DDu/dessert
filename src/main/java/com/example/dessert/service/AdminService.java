package com.example.dessert.service;

import com.example.dessert.domain.CustomsStatus;
import com.example.dessert.domain.Order;
import com.example.dessert.domain.OrderStatus;
import com.example.dessert.domain.Product;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AdminService {

    private final MockDataService mockDataService;
    private final OrderService orderService;

    public AdminService(MockDataService mockDataService, OrderService orderService) {
        this.mockDataService = mockDataService;
        this.orderService = orderService;
    }

    public Product getProduct() {
        return mockDataService.getProduct();
    }

    public void updateSecuredStock(int securedStock) {
        mockDataService.getProduct().setSecuredStock(securedStock);
    }

    public void updateCustomsStatus(CustomsStatus customsStatus) {
        mockDataService.getProduct().setCustomsStatus(customsStatus);

        if (customsStatus == CustomsStatus.FAILED) {
            mockDataService.getOrders().stream()
                    .filter(order -> order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.WAITLIST)
                    .forEach(order -> {
                        order.setStatus(OrderStatus.CANCELLED);
                        orderService.addNotification(order.getId(),
                                order.getCustomerName() + " 주문은 통관 실패로 취소되었습니다.");
                    });
        }
    }

    public void confirmOrders() {
        Product product = mockDataService.getProduct();

        if (product.getCustomsStatus() != CustomsStatus.CLEARED) {
            return;
        }

        List<Order> sortedOrders = mockDataService.getOrders().stream()
                .filter(order -> order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.WAITLIST)
                .sorted(Comparator.comparing(Order::getOrderedAt))
                .toList();

        int remaining = product.getSecuredStock();

        for (Order order : sortedOrders) {
            if (remaining >= order.getQuantity()) {
                order.setStatus(OrderStatus.CONFIRMED);
                remaining -= order.getQuantity();
                orderService.addNotification(order.getId(),
                        order.getCustomerName() + " 주문이 통관 완료 후 확정되었습니다.");
            } else {
                order.setStatus(OrderStatus.WAITLIST);
                orderService.addNotification(order.getId(),
                        order.getCustomerName() + " 주문은 확보 수량 부족으로 대기 처리되었습니다.");
            }
        }
    }
}
