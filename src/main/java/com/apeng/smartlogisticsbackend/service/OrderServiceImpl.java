package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.OrderRequest;
import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.entity.Product;
import com.apeng.smartlogisticsbackend.repository.OrderRepository;
import com.apeng.smartlogisticsbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Long insert(Order order) {
        return orderRepository.save(order).getId();
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order update(Order order) {
        if (order.getId() == null) return null;
        return orderRepository.save(order);
    }

    @Override
    public Order submit(OrderRequest orderRequest) {
        Product product = productRepository.findById(orderRequest.productId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find the product in database!"));
        if (orderRequest.productNum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product number cannot be lower or equal to 0");
        }
        Order order = new Order(product, orderRequest.productNum());
        return orderRepository.save(order);
    }

}
