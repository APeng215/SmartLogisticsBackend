package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.OrderRequest;
import com.apeng.smartlogisticsbackend.dto.OrderResponse;
import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.entity.Product;
import com.apeng.smartlogisticsbackend.repository.OrderRepository;
import com.apeng.smartlogisticsbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WarehouseService warehouseService;

    @Override
    public Long insert(Order order) {
        return orderRepository.save(order).getId();
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponse findById(Long id) {
        return new OrderResponse(orderRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    public List<OrderResponse> findAll() {
        List<OrderResponse> orderResponses = new ArrayList<>();
        orderRepository.findAll().forEach(order -> {
            orderResponses.add(new OrderResponse(order));
        });
        return orderResponses;
    }

    @Override
    public OrderResponse update(Order order) {
        if (order.getId() == null) return null;
        return new OrderResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse submit(OrderRequest orderRequest) {
        validateProductNum(orderRequest);
        return doSubmit(orderRequest);
    }

    @Override
    public List<OrderResponse> findByState(String state) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        orderRepository.findOrdersByState(state).forEach(order->{
            orderResponses.add(new OrderResponse(order));
        });
        return orderResponses;
    }

    @Override
    public void deleteByIdList(List<Long> idList) {
        orderRepository.deleteAllById(idList);
    }

    @Override
    public List<Order> findOrdersByCarId(long id) {
        return orderRepository.findOrdersByCarId(id);
    }

    @Override
    public List<OrderResponse> findOrdersByShelveId(long shelveId) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        orderRepository.findOrdersByShelveId(shelveId).forEach(order -> {
            orderResponses.add(new OrderResponse(order));
        });
        return orderResponses;
    }

    private OrderResponse doSubmit(OrderRequest orderRequest) {
        Product product = productRepository.findById(orderRequest.productId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find the product in database!"));
        Order order = new Order(product, orderRequest.productNum(), warehouseService.findById(orderRequest.targetWarehouseId()));
        return new OrderResponse(orderRepository.save(order));
    }

    private static void validateProductNum(OrderRequest orderRequest) {
        if (orderRequest.productNum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product number cannot be lower or equal to 0");
        }
    }

}
