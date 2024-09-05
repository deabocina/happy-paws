package com.webshop.happy.paws.repository;

import com.webshop.happy.paws.entity.Brand;
import com.webshop.happy.paws.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Filter
    List<Product> findByPetType(String petType);

    List<Product> findByPetTypeAndCategory(String petType, String category);

    List<Product> findByPetTypeAndSubcategory(String petType, String subcategory);

    List<Product> findByPetTypeAndBrand(String petType, Brand brand);

    List<Product> findByPetTypeAndCategoryAndBrand(String petType, String category, Brand brand);

    List<Product> findByPetTypeAndCategoryAndSubcategory(String petType, String category, String subcategory);

    List<Product> findByPetTypeAndSubcategoryAndBrand(String petType, String subcategory, Brand brand);

    List<Product> findByPetTypeAndCategoryAndSubcategoryAndBrand(String petType, String category, String subcategory, Brand brand);

    // Search
    List<Product> findAllByNameContainingIgnoreCase(String name);

    // Discount
    List<Product> findByBrandId(UUID brandId);

}