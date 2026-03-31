package com.example.dessert.controller;

import com.example.dessert.service.AdminService;
import com.example.dessert.service.CatalogService;
import com.example.dessert.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final AdminService adminService;
    private final OrderService orderService;
    private final CatalogService catalogService;

    public PageController(AdminService adminService, OrderService orderService, CatalogService catalogService) {
        this.adminService = adminService;
        this.orderService = orderService;
        this.catalogService = catalogService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("catalogProducts", catalogService.getCatalogProducts());
        model.addAttribute("featuredProduct", catalogService.getFeaturedProduct());
        model.addAttribute("orders", orderService.getOrders());
        model.addAttribute("notifications", orderService.getNotifications());
        return "index";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("product", adminService.getProduct());
        model.addAttribute("orders", orderService.getOrders());
        return "orders";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("product", adminService.getProduct());
        model.addAttribute("orders", orderService.getOrders());
        model.addAttribute("notifications", orderService.getNotifications());
        return "admin";
    }
}
