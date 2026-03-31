package com.example.dessert.controller;

import com.example.dessert.dto.ApiResponse;
import com.example.dessert.dto.CreateOrderRequest;
import com.example.dessert.dto.UpdateStatusRequest;
import com.example.dessert.dto.UpdateStockRequest;
import com.example.dessert.service.AdminService;
import com.example.dessert.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final OrderService orderService;
    private final AdminService adminService;

    public ApiController(OrderService orderService, AdminService adminService) {
        this.orderService = orderService;
        this.adminService = adminService;
    }

    @PostMapping("/orders")
    public ApiResponse createOrder(@RequestBody CreateOrderRequest request) {
        if (request.getCustomerName() == null || request.getCustomerName().isBlank()) {
            return new ApiResponse(false, "고객명을 입력해 주세요.");
        }
        if (request.getQuantity() <= 0) {
            return new ApiResponse(false, "수량은 1개 이상이어야 합니다.");
        }

        orderService.createOrder(request);
        return new ApiResponse(true, "선주문이 접수되었습니다.");
    }

    @PostMapping("/orders/{orderId}/cancel")
    public ApiResponse cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return new ApiResponse(true, "주문이 취소되었습니다.");
    }

    @PostMapping("/admin/customs-status")
    public ApiResponse updateCustomsStatus(@RequestBody UpdateStatusRequest request) {
        adminService.updateCustomsStatus(request.getCustomsStatus());
        return new ApiResponse(true, "통관 상태가 변경되었습니다.");
    }

    @PostMapping("/admin/secured-stock")
    public ApiResponse updateSecuredStock(@RequestBody UpdateStockRequest request) {
        adminService.updateSecuredStock(request.getSecuredStock());
        return new ApiResponse(true, "확보 수량이 변경되었습니다.");
    }

    @PostMapping("/admin/confirm-orders")
    public ApiResponse confirmOrders() {
        adminService.confirmOrders();
        return new ApiResponse(true, "주문 확정 로직이 실행되었습니다.");
    }
}
