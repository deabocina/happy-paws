package com.webshop.happy.paws.data;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductData {

    private Integer stockQuantity;
    private MultipartFile productImage;
    private String name;
    private String petType;
    private String category;
    private String subcategory;
    private UUID brand;
    private BigDecimal price;
    private BigDecimal currentRating;
    private Integer taxAmount;
    private Integer numberOfBuyers;

}
