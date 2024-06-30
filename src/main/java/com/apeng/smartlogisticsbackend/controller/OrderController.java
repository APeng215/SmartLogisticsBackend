package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.dto.OrderRequest;
import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Operation(summary = "通过ID获取订单信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到订单",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "404", description = "订单未找到",
                    content = @Content)
    })
    @GetMapping("/search/id/{id}")
    public Order getOrderById(@Parameter(description = "订单的ID") @PathVariable long id) {
        return orderService.findById(id);
    }

    @Operation(summary = "获取所有订单信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到订单",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))})
    })
    @GetMapping("/search")
    public List<Order> getAllOrder() {
        return orderService.findAll();
    }

    @Operation(summary = "添加新订单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "订单添加成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))})
    })
    @PostMapping("/insert")
    public Long insertOrder(@Parameter(description = "订单信息") @RequestBody Order order) {
        return orderService.insert(order);
    }

    @Operation(summary = "通过ID删除订单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "订单删除成功"),
            @ApiResponse(responseCode = "404", description = "订单未找到",
                    content = @Content)
    })
    @DeleteMapping("/delete/id/{id}")
    public void deleteOrderById(@Parameter(description = "订单的ID") @PathVariable long id) {
        orderService.deleteById(id);
    }

    @Operation(summary = "更新订单信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "订单更新成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))})
    })
    @PutMapping("/update")
    public Order updateOrder(@Parameter(description = "订单信息") @RequestBody Order order) {
        return orderService.update(order);
    }

    @Operation(summary = "提交订单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "订单提交成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))})
    })
    @PostMapping("/submit")
    public Order submitOrder(@Parameter(description = "订单请求") @RequestBody OrderRequest orderRequest) {
        return orderService.submit(orderRequest);
    }
}
