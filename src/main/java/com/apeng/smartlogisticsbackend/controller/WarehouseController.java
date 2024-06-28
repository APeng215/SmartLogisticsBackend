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

    @GetMapping("/warehouse/search/id/{id}")
    Warehouse getWarehouseById(@PathVariable long id) {
        return warehouseService.findById(id);
    }

    @GetMapping("/warehouse/search/name/{name}")
    List<Warehouse> getWarehousesByName(@PathVariable String name) {
        return warehouseService.findByName(name);
    }

    @GetMapping("/warehouse/search/position/{position}")
    List<Warehouse> getWarehousesByPosition(@PathVariable String position) {
        return warehouseService.findByPosition(position);
    }
    @GetMapping("/warehouse/search")
    List<Warehouse> getAllWarehouse()
    {
        return warehouseService.findAll();
    }

    @PostMapping("/warehouse/insert")
    Long insertWarehouse(@RequestBody Warehouse warehouse)
    {
        return warehouseService.insert(warehouse);
    }

    @DeleteMapping("/warehouse/delete/id/{id}")
    void deleteWarehouseById(@PathVariable long id)
    {
        warehouseService.deleteById(id);
    }

    @PutMapping("/warehouse/update")
    Warehouse updateWarehouse(@RequestBody Warehouse warehouse)
    {
        return warehouseService.update(warehouse);
    }
}
