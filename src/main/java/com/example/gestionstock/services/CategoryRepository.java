package com.example.gestionstock.services;

import com.example.gestionstock.entity.Category;
import com.example.gestionstock.entity.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
  List<Category> findCategoriesByCategoryStatus(CategoryStatus categoryStatus);
}
