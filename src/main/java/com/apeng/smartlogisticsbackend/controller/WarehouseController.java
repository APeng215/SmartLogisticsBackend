package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.dto.InboundRequest;
import com.apeng.smartlogisticsbackend.dto.OutboundRequest;
import com.apeng.smartlogisticsbackend.entity.Warehouse;
import com.apeng.smartlogisticsbackend.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    @Operation(summary = "通过ID获取仓库信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到仓库",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Warehouse.class))}),
            @ApiResponse(responseCode = "404", description = "仓库未找到",
                    content = @Content)
    })
    @GetMapping("/search/id/{id}")
    public Warehouse getWarehouseById(@Parameter(description = "仓库的ID") @PathVariable long id) {
        return warehouseService.findById(id);
    }

    @Operation(summary = "通过名称获取仓库列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到仓库",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Warehouse.class))}),
            @ApiResponse(responseCode = "404", description = "仓库未找到",
                    content = @Content)
    })
    @GetMapping("/search/name/{name}")
    public List<Warehouse> getWarehousesByName(@Parameter(description = "仓库名称") @PathVariable String name) {
        return warehouseService.findByName(name);
    }

    @Operation(summary = "通过位置获取仓库列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到仓库",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Warehouse.class))}),
            @ApiResponse(responseCode = "404", description = "仓库未找到",
                    content = @Content)
    })
    @GetMapping("/search/position/{position}")
    public List<Warehouse> getWarehousesByPosition(@Parameter(description = "仓库位置") @PathVariable String position) {
        return warehouseService.findByPosition(position);
    }

    @Operation(summary = "获取所有仓库信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到仓库",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Warehouse.class))})
    })
    @GetMapping("/search")
    public List<Warehouse> getAllWarehouse() {
        return warehouseService.findAll();
    }

    @Operation(summary = "添加新仓库")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "仓库添加成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))})
    })
    @PostMapping("/insert")
    public Long insertWarehouse(@Parameter(description = "仓库信息") @RequestBody Warehouse warehouse) {
        return warehouseService.insert(warehouse);
    }

    @Operation(summary = "通过ID删除仓库")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "仓库删除成功"),
            @ApiResponse(responseCode = "404", description = "仓库未找到",
                    content = @Content)
    })
    @DeleteMapping("/delete/id/{id}")
    public void deleteWarehouseById(@Parameter(description = "仓库的ID") @PathVariable long id) {
        warehouseService.deleteById(id);
    }

    @Operation(summary = "更新仓库信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "仓库更新成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Warehouse.class))})
    })
    @PutMapping("/update")
    public Warehouse updateWarehouse(@Parameter(description = "仓库信息") @RequestBody Warehouse warehouse) {
        return warehouseService.update(warehouse);
    }

    @Operation(summary = "入库操作")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "入库成功")
    })
    @PostMapping("/inbound")
    public void inbound(@Parameter(description = "入库请求") @RequestBody InboundRequest inboundRequest) {
        warehouseService.inbound(inboundRequest);
    }

    @Operation(summary = "出库操作")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "出库成功")
    })
    @PostMapping("/outbound")
    public void outbound(@Parameter(description = "出库请求") @RequestBody OutboundRequest outboundRequest) {
        warehouseService.outbound(outboundRequest);
    }
}
