package com.webshop.happy.paws.mapper;

import com.webshop.happy.paws.data.ProductData;
import com.webshop.happy.paws.entity.Brand;
import com.webshop.happy.paws.entity.Product;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public class ProductMapper {
    public static Product mapDataEntity(ProductData productData){

        Product product = new Product();
        product.setStockQuantity(productData.getStockQuantity());
        product.setProductImage(String.valueOf(productData.getProductImage()));
        product.setName(productData.getName());
        product.setPetType(productData.getPetType());
        product.setCategory(productData.getCategory());
        product.setSubcategory(productData.getSubcategory());

        if (productData.getBrand() != null) {
            Brand brand = new Brand();
            brand.setId(productData.getBrand());
            product.setBrand(brand);
        }

        product.setPrice(productData.getPrice());
        product.setCurrentRating(productData.getCurrentRating());
        product.setTaxAmount(productData.getTaxAmount());
        product.setNumberOfBuyers(productData.getNumberOfBuyers());

        return product;

    }
}
