package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/order/{id}")
    Order getOrderById(@PathVariable long id) {
        return orderService.findById(id);
    }

    @GetMapping("/order")
    List<Order> getAllOrder()
    {
        return orderService.findAll();
    }

    @PostMapping("/order")
    Long insertOrder(@RequestBody Order order)
    {
        return orderService.insert(order);
    }

    @DeleteMapping("/order/{id}")
    void deleteOrderById(@PathVariable long id)
    {
        orderService.deleteById(id);
    }

    @PutMapping("/order")
    Order updateOrder(@RequestBody Order order)
    {
        return orderService.update(order);
    }
}
