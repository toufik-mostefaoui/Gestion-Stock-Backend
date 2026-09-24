package com.example.gestionstock.services;

import com.example.gestionstock.dto.ProductDTO;
import com.example.gestionstock.entity.Category;
import com.example.gestionstock.entity.CategoryStatus;
import com.example.gestionstock.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository){
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> findAllActiveCategories() {
        return categoryRepository.findCategoriesByCategoryStatus(CategoryStatus.ACTIVE);
    }

    public List<Category> findAllCategories(){
        return categoryRepository.findAll();
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Category createCategory(Category category) {
        category.setCategoryStatus(CategoryStatus.ACTIVE);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category category) {

        Category existingCategory = findCategoryById(id);

        existingCategory.setName(category.getName());
        existingCategory.setCategoryStatus(category.getCategoryStatus());

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {

        Category category = findCategoryById(id);
        category.setCategoryStatus(CategoryStatus.INACTIVE);

        categoryRepository.save(category);
    }


    public Product createProduct(ProductDTO pro) {

        List<Category> categories = categoryRepository.findAllById(pro.getCategoryIds());

        Product product = new Product();

        product.setName(pro.getName());
        product.setDescription(pro.getDescription());
        product.setPrice(pro.getPrice());
        product.setStock(pro.getStock());
        product.setCategoryList(categories);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDTO product) {

        Product existingProduct = findProductById(id);

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategoryList(categoryRepository.findAllById(product.getCategoryIds()));

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {

        Product product = findProductById(id);

        productRepository.delete(product);
    }

    public void purchaseProduct(Long id){
        Product product = findProductById(id);

        product.setStock(product.getStock() - 1);
        productRepository.save(product);

    }

}
