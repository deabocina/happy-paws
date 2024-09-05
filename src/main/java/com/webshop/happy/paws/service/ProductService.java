package com.webshop.happy.paws.service;

import com.webshop.happy.paws.data.ProductData;
import com.webshop.happy.paws.entity.Brand;
import com.webshop.happy.paws.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {

    List<Product> getAllProducts();

    List<Product> getDogProducts();

    List<Product> getCatProducts();

    List<Product> getSmallAnimalProducts();

    List<Product> getFilteredProducts(String petType, String category, String subcategory, Brand brand);

    Product getProductById(UUID productId);

    void saveProduct(Product product);

    List<Product> getProductsByOrderId(UUID orderId);

    // Search
    List<Product> getSearchProducts(String name);

    // Staff
    void addProduct(ProductData productData, Brand brand, MultipartFile productImage);

    String saveProductImage(MultipartFile productImage) throws IOException;

    // Admin
    int getCurrentTaxAmount();

    void updateTaxAmount(int newTaxAmount);


}
