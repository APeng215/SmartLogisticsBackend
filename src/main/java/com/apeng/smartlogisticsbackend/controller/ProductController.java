package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Product;
import com.apeng.smartlogisticsbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/product/search/id/{id}")
    Product getProductById(@PathVariable long id) {
        return productService.findById(id);
    }

    @GetMapping("/product/search/name/{name}")
    List<Product> getProductsByName(@PathVariable String name) {
        return productService.findByName(name);
    }

    @GetMapping("/product/search")
    List<Product> getAllProduct()
    {
        return productService.findAll();
    }

    @PostMapping("/product/insert")
    Long insertProduct(@RequestBody Product product)
    {
        return productService.insert(product);
    }

    @DeleteMapping("/product/delete/id/{id}")
    void deleteProductById(@PathVariable long id)
    {
        productService.deleteById(id);
    }

    @PutMapping("/product/update")
    Product updateProduct(@RequestBody Product product)
    {
        return productService.update(product);
    }
}
