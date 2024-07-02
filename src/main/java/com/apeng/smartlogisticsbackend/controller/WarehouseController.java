package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.dto.InboundRequest;
import com.apeng.smartlogisticsbackend.dto.OutboundRequest;
import com.apeng.smartlogisticsbackend.entity.Warehouse;
import com.apeng.smartlogisticsbackend.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    @Operation(summary = "通过ID获取仓库信息")
    @GetMapping("/search/id/{id}")
    public Warehouse getWarehouseById(@PathVariable long id) {
        return warehouseService.findById(id);
    }

    @Operation(summary = "通过名称获取仓库列表")
    @GetMapping("/search/name/{name}")
    public List<Warehouse> getWarehousesByName(@PathVariable String name) {
        return warehouseService.findByName(name);
    }

    @Operation(summary = "通过位置获取仓库列表")
    @GetMapping("/search/position/{position}")
    public List<Warehouse> getWarehousesByPosition(@PathVariable String position) {
        return warehouseService.findByPosition(position);
    }

    @Operation(summary = "获取所有仓库信息")
    @GetMapping("/search")
    public List<Warehouse> getAllWarehouse() {
        return warehouseService.findAll();
    }

    @Operation(summary = "添加新仓库")
    @PostMapping("/insert")
    public Long insertWarehouse(@RequestBody Warehouse warehouse) {
        return warehouseService.insert(warehouse);
    }

    @Operation(summary = "通过ID删除仓库")
    @DeleteMapping("/delete/id/{id}")
    public void deleteWarehouseById(@PathVariable long id) {
        warehouseService.deleteById(id);
    }

    @Operation(summary = "通过仓库的ID列表删除仓库")
    @DeleteMapping("/delete/idList")
    public void deleteWarehouseByIdList(@RequestBody List<Long> idList){
        warehouseService.deleteByIdList(idList);
    }

    @Operation(summary = "更新仓库信息")
    @PutMapping("/update")
    public Warehouse updateWarehouse(@RequestBody Warehouse warehouse) {
        return warehouseService.update(warehouse);
    }

    @Operation(summary = "入库操作")
    @PutMapping("/inbound")
    public void inbound(@RequestBody InboundRequest inboundRequest) {
        warehouseService.inbound(inboundRequest);
    }

    @Operation(summary = "出库操作")
    @PutMapping("/outbound")
    public void outbound(@RequestBody OutboundRequest outboundRequest) {
        warehouseService.outbound(outboundRequest);
    }
}
