package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Product;
import com.apeng.smartlogisticsbackend.service.ProductService;
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
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(summary = "通过ID获取产品信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到产品",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "产品未找到",
                    content = @Content)
    })
    @GetMapping("/search/id/{id}")
    public Product getProductById(@Parameter(description = "产品的ID") @PathVariable long id) {
        return productService.findById(id);
    }

    @Operation(summary = "通过名称获取产品列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到产品",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "产品未找到",
                    content = @Content)
    })
    @GetMapping("/search/name/{name}")
    public List<Product> getProductsByName(@Parameter(description = "产品名称") @PathVariable String name) {
        return productService.findByName(name);
    }

    @Operation(summary = "获取所有产品信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到产品",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))})
    })
    @GetMapping("/search")
    public List<Product> getAllProduct() {
        return productService.findAll();
    }

    @Operation(summary = "添加新产品")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "产品添加成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))})
    })
    @PostMapping("/insert")
    public Long insertProduct(@Parameter(description = "产品信息") @RequestBody Product product) {
        return productService.insert(product);
    }

    @Operation(summary = "通过ID删除产品")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "产品删除成功"),
            @ApiResponse(responseCode = "404", description = "产品未找到",
                    content = @Content)
    })
    @DeleteMapping("/delete/id/{id}")
    public void deleteProductById(@Parameter(description = "产品的ID") @PathVariable long id) {
        productService.deleteById(id);
    }

    @Operation(summary = "更新产品信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "产品更新成功",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))})
    })
    @PutMapping("/update")
    public Product updateProduct(@Parameter(description = "产品信息") @RequestBody Product product) {
        return productService.update(product);
    }
}
