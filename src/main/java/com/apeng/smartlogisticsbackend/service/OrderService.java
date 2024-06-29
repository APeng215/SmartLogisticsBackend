package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.OrderRequest;
import com.apeng.smartlogisticsbackend.entity.Order;

import java.util.List;

public interface OrderService {
    Long insert(Order order);
    void deleteById(Long id);
    Order findById(Long id);
    List<Order> findAll();
    Order update(Order order);
    Order submit(OrderRequest order);
}
