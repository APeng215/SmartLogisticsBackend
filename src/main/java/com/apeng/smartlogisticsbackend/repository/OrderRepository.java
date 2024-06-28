package com.apeng.smartlogisticsbackend.repository;

import com.apeng.smartlogisticsbackend.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
