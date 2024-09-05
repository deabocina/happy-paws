package com.webshop.happy.paws.entity;

import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "product_image")
    private String productImage;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "pet_type", length = 20)
    private String petType;

    @Column(name = "category", length = 40)
    private String category;

    @Column(name = "subcategory", length = 40)
    private String subcategory;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private BigDecimal price;

    @Column(name = "current_rating", precision = 2, scale = 1)
    private BigDecimal currentRating;

    @Column(name = "tax_amount")
    private Integer taxAmount;

    @Column(name = "number_of_buyers")
    private Integer numberOfBuyers;

}
