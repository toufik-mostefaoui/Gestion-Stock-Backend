package com.example.gestionstock.dto;


import com.example.gestionstock.entity.Category;
import lombok.Data;

@Data
public class ProductDTO {

    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Long categoryId;

}