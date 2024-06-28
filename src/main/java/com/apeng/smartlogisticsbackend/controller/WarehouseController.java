package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Warehouse;
import com.apeng.smartlogisticsbackend.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/warehouse/{id}")
    Warehouse getWarehouseById(@PathVariable long id) {
        return warehouseService.findById(id);
    }

    @GetMapping("/warehouse")
    List<Warehouse> getAllWarehouse()
    {
        return warehouseService.findAll();
    }

    @PostMapping("/warehouse")
    Long insertWarehouse(@RequestBody Warehouse warehouse)
    {
        return warehouseService.insert(warehouse);
    }

    @DeleteMapping("/warehouse/{id}")
    void deleteWarehouseById(@PathVariable long id)
    {
        warehouseService.deleteById(id);
    }

    @PutMapping("/warehouse")
    Warehouse updateWarehouse(@RequestBody Warehouse warehouse)
    {
        return warehouseService.update(warehouse);
    }
}
