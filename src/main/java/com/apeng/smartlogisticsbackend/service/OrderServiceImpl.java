package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository repository;

    @Override
    public Long insert(Order order) {
        return repository.save(order).getId();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Order findById(Long id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Order update(Order order) {
        if (order.getId() == null) return null;
        return repository.save(order);
    }
}
