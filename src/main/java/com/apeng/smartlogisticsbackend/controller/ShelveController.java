package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Shelve;
import com.apeng.smartlogisticsbackend.service.ShelveService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelve")
public class ShelveController {
    @Autowired
    private ShelveService shelveService;

    @Operation(summary = "通过ID获取货架信息")
    @GetMapping("/search/id/{id}")
    public Shelve getShelveById(@PathVariable long id) {
        return shelveService.findById(id);
    }

    @Operation(summary = "获取所有货架信息")
    @GetMapping("/search")
    public List<Shelve> getAllShelve() {
        return shelveService.findAll();
    }

    @Operation(summary = "添加新货架")
    @PostMapping("/insert")
    public Long insertShelve(@RequestBody Shelve shelve) {
        return shelveService.insert(shelve);
    }

    @Operation(summary = "通过ID删除货架")
    @DeleteMapping("/delete/id/{id}")
    public void deleteShelveById(@PathVariable long id) {
        shelveService.deleteById(id);
    }

    @Operation(summary = "通过货架的ID列表删除货架")
    @DeleteMapping("/delete/idList")
    public void deleteShelveByIdList(@RequestBody List<Long> idList){
        shelveService.deleteByIdList(idList);
    }

    @Operation(summary = "更新货架信息")
    @PutMapping("/update")
    public Shelve updateShelve(@RequestBody Shelve shelve) {
        return shelveService.update(shelve);
    }

    @Operation(summary = "通过仓库ID获取其拥有的货架信息")
    @GetMapping("/search/{warehouseId}")
    public List<Shelve> findShelvesByWarehouseId(@PathVariable Long warehouseId) {
        return shelveService.findShelvesByWarehouseId(warehouseId);
    }

}
