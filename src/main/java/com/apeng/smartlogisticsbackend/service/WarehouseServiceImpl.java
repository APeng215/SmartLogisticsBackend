package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.entity.Warehouse;
import com.apeng.smartlogisticsbackend.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository repository;

    @Override
    public Long insert(Warehouse warehouse) {
        return repository.save(warehouse).getId();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Warehouse findById(Long id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Warehouse> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Warehouse update(Warehouse warehouse) {
        if (warehouse.getId() == null) return null;
        return repository.save(warehouse);
    }

}
