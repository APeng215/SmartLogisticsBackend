package com.apeng.smartlogisticsbackend.controller;

import com.apeng.smartlogisticsbackend.entity.Product;
import com.apeng.smartlogisticsbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/search/id/{id}")
    Product getProductById(@PathVariable long id) {
        return productService.findById(id);
    }

    @GetMapping("/search/name/{name}")
    List<Product> getProductsByName(@PathVariable String name) {
        return productService.findByName(name);
    }

    @GetMapping("/search")
    List<Product> getAllProduct()
    {
        return productService.findAll();
    }

    @PostMapping("/insert")
    Long insertProduct(@RequestBody Product product)
    {
        return productService.insert(product);
    }

    @DeleteMapping("/delete/id/{id}")
    void deleteProductById(@PathVariable long id)
    {
        productService.deleteById(id);
    }

    @PutMapping("/update")
    Product updateProduct(@RequestBody Product product)
    {
        return productService.update(product);
    }
}
