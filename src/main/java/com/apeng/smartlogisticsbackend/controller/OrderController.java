package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.dto.OrderRequest;
import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/search/id/{id}")
    Order getOrderById(@PathVariable long id) {
        return orderService.findById(id);
    }

    @GetMapping("/search")
    List<Order> getAllOrder()
    {
        return orderService.findAll();
    }

    @PostMapping("/insert")
    Long insertOrder(@RequestBody Order order)
    {
        return orderService.insert(order);
    }

    @DeleteMapping("/delete/id/{id}")
    void deleteOrderById(@PathVariable long id)
    {
        orderService.deleteById(id);
    }

    @PutMapping("/update")
    Order updateOrder(@RequestBody Order order)
    {
        return orderService.update(order);
    }

    @PostMapping("/submit")
    Order submitOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.submit(orderRequest);
    }

}
