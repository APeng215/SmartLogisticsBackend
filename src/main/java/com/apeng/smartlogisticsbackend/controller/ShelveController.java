package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Shelve;
import com.apeng.smartlogisticsbackend.service.ShelveService;
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
@RequestMapping("/shelve")
public class ShelveController {
    @Autowired
    private ShelveService shelveService;

    @Operation(summary = "通过ID获取货架信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到货架",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Shelve.class))}),
            @ApiResponse(responseCode = "404", description = "货架未找到",
                    content = @Content)
    })
    @GetMapping("/search/id/{id}")
    public Shelve getShelveById(@Parameter(description = "货架的ID") @PathVariable long id) {
        return shelveService.findById(id);
    }

    @Operation(summary = "获取所有货架信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到货架",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Shelve.class))})
    })
    @GetMapping("/search")
    public List<Shelve> getAllShelve() {
        return shelveService.findAll();
    }

    @Operation(summary = "添加新货架")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "货架添加成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))})
    })
    @PostMapping("/insert")
    public Long insertShelve(@Parameter(description = "货架信息") @RequestBody Shelve shelve) {
        return shelveService.insert(shelve);
    }

    @Operation(summary = "通过ID删除货架")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "货架删除成功"),
            @ApiResponse(responseCode = "404", description = "货架未找到",
                    content = @Content)
    })
    @DeleteMapping("/delete/id/{id}")
    public void deleteShelveById(@Parameter(description = "货架的ID") @PathVariable long id) {
        shelveService.deleteById(id);
    }

    @Operation(summary = "更新货架信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "货架更新成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Shelve.class))})
    })
    @PutMapping("/update")
    public Shelve updateShelve(@Parameter(description = "货架信息") @RequestBody Shelve shelve) {
        return shelveService.update(shelve);
    }
}
