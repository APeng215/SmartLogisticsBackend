package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.entity.Order;
import com.apeng.smartlogisticsbackend.entity.Shelve;

import java.util.List;

public interface ShelveService {
    Long insert(Shelve shelve);
    void deleteById(Long id);
    Shelve findById(Long id);
    List<Shelve> findAll();
    List<Shelve> findShelvesByWarehouseId(Long id);
    Shelve update(Shelve shelve);
    boolean canAddOrder(Shelve shelve, Order order);

}
