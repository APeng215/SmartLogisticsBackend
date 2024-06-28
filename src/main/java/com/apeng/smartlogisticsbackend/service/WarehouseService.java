package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.entity.Warehouse;

import java.util.List;

public interface WarehouseService {

    Long insert(Warehouse warehouse);
    void deleteById(Long id);
    Warehouse findById(Long id);
    List<Warehouse> findAll();
    Warehouse update(Warehouse warehouse);

}
