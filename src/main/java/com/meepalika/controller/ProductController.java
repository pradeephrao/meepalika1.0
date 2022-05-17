package com.meepalika.controller;

import com.meepalika.entity.Product;
import com.meepalika.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = { "", "/" })
    public @NotNull Iterable<Product> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(value = { "", "/{id}" })
    public @NotNull Product getProduct(@PathVariable long id) {
        return productService.getProduct(id);
    }


    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void addProduct(@RequestBody Product product){
        productService.save(product);
    }


    @PutMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updateProduct(@RequestBody Product product){
        productService.save(product);
    }
}
