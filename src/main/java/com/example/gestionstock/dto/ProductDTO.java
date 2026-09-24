package com.example.gestionstock.dto;


import com.example.gestionstock.entity.Category;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {

    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private List<Long> categoryIds;

}