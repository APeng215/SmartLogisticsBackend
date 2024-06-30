package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.dto.OrderRequest;
import com.apeng.smartlogisticsbackend.dto.OrderResponse;
import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Operation(summary = "通过ID获取订单信息")
    @GetMapping("/search/id/{id}")
    public OrderResponse getOrderById(@PathVariable long id) {
        return orderService.findById(id);
    }

    @Operation(summary = "获取所有订单信息")
    @GetMapping("/search")
    public List<OrderResponse> getAllOrder() {
        return orderService.findAll();
    }

    @Operation(summary = "添加新订单")
    @PostMapping("/insert")
    public Long insertOrder(@RequestBody Order order) {
        return orderService.insert(order);
    }

    @Operation(summary = "通过ID删除订单")
    @DeleteMapping("/delete/id/{id}")
    public void deleteOrderById(@PathVariable long id) {
        orderService.deleteById(id);
    }

    @Operation(summary = "更新订单信息")
    @PutMapping("/update")
    public OrderResponse updateOrder(@RequestBody Order order) {
        return orderService.update(order);
    }

    @Operation(summary = "提交订单")
    @PostMapping("/submit")
    public OrderResponse submitOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.submit(orderRequest);
    }
}
