package com.example.gestionstock.controller;


import com.example.gestionstock.dto.ProductDTO;
import com.example.gestionstock.entity.Category;
import com.example.gestionstock.entity.Product;
import com.example.gestionstock.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/products/categories")
    public List<Category> findAllCategories() {
        return productService.findAllCategories();
    }

    @GetMapping("/products/active/categories")
    public List<Category> findAllActiveCategories(){
        return productService.findAllActiveCategories();
    }

    @GetMapping("/products/categories/{id}")
    public Category findCategoryById(@PathVariable Long id) {
        return productService.findCategoryById(id);
    }

    @PostMapping("/admin/categories")
    public Category createCategory(@RequestBody Category category) {

        return productService.createCategory(category);
    }

    @PutMapping("/admin/categories/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return productService.updateCategory(id, category);
    }

    @DeleteMapping("/admin/categories/{id}")
    public String deleteCategory(@PathVariable Long id) {
        productService.deleteCategory(id);
        return "Category deleted successfully";
    }

    @GetMapping("/products")
    public List<Product> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product findProductById(@PathVariable Long id) {
        return productService.findProductById(id);
    }

    @PostMapping("/admin/products")
    public Product createProduct(@RequestBody ProductDTO request) {
        return productService.createProduct(request);
    }

    @PutMapping("/admin/products/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/admin/products/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }

    @PostMapping("/buy/{id}")
    public String buyProduct(@PathVariable Long id) {
        productService.purchaseProduct(id);

        return "Product purchased successfully";
    }
}