package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Product;
import com.apeng.smartlogisticsbackend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(summary = "通过ID获取产品信息")
    @GetMapping("/search/id/{id}")
    public Product getProductById(@PathVariable long id) {
        return productService.findById(id);
    }

    @Operation(summary = "通过名称获取产品列表")
    @GetMapping("/search/name/{name}")
    public List<Product> getProductsByName(@PathVariable String name) {
        return productService.findByName(name);
    }

    @Operation(summary = "获取所有产品信息")
    @GetMapping("/search")
    public List<Product> getAllProduct() {
        return productService.findAll();
    }

    @Operation(summary = "添加新产品")
    @PostMapping("/insert")
    public Long insertProduct(@RequestBody Product product) {
        return productService.insert(product);
    }

    @Operation(summary = "通过ID删除产品")
    @DeleteMapping("/delete/id/{id}")
    public void deleteProductById(@PathVariable long id) {
        productService.deleteById(id);
    }

    @Operation(summary = "更新产品信息")
    @PutMapping("/update")
    public Product updateProduct(@RequestBody Product product) {
        return productService.update(product);
    }
}
