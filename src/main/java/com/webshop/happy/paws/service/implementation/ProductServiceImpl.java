package com.webshop.happy.paws.service.implementation;

import com.webshop.happy.paws.data.ProductData;
import com.webshop.happy.paws.entity.Brand;
import com.webshop.happy.paws.entity.OrderItem;
import com.webshop.happy.paws.entity.Product;
import com.webshop.happy.paws.repository.OrderItemRepository;
import com.webshop.happy.paws.repository.ProductRepository;
import com.webshop.happy.paws.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getDogProducts() {
        return productRepository.findByPetType("Dog");
    }

    @Override
    public List<Product> getCatProducts() {
        return productRepository.findByPetType("Cat");
    }

    @Override
    public List<Product> getSmallAnimalProducts() {
        return productRepository.findByPetType("Small-Animal");
    }

    @Override
    public List<Product> getFilteredProducts(String petType, String category, String subcategory, Brand brand) {
        if ("ALL".equalsIgnoreCase(category)) {
            if ("ALL".equalsIgnoreCase(subcategory)) {
                if (brand != null) {
                    return productRepository.findByPetTypeAndBrand(petType, brand);
                } else {
                    return productRepository.findByPetType(petType);
                }
            } else {
                if (brand != null) {
                    return productRepository.findByPetTypeAndSubcategoryAndBrand(petType, subcategory, brand);
                } else {
                    return productRepository.findByPetTypeAndSubcategory(petType, subcategory);
                }
            }
        } else {
            if (brand != null) {
                if ("ALL".equalsIgnoreCase(subcategory)) {
                    return productRepository.findByPetTypeAndCategoryAndBrand(petType, category, brand);
                } else {
                    return productRepository.findByPetTypeAndCategoryAndSubcategoryAndBrand(petType, category, subcategory, brand);
                }
            } else {
                if ("ALL".equalsIgnoreCase(subcategory)) {
                    return productRepository.findByPetTypeAndCategory(petType, category);
                } else {
                    return productRepository.findByPetTypeAndCategoryAndSubcategory(petType, category, subcategory);
                }
            }
        }
    }

    @Override
    public Product getProductById(UUID productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }


    @Override
    public List<Product> getProductsByOrderId(UUID orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        List<Product> products = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            products.add(product);
        }
        return products;
    }


    // Search
    @Override
    public List<Product> getSearchProducts(String name) {
        if (name == null || name.isEmpty()) {
            return new ArrayList<>();
        }

        return productRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    public String saveProductImage(MultipartFile productImage) throws IOException {
        String fileName = StringUtils.cleanPath(productImage.getOriginalFilename());
        String uploadDir = "src/main/resources/static/img";

        File uploadPath = new File(uploadDir);

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File destFile = new File(uploadPath.getAbsolutePath() + File.separator + fileName);

        try (OutputStream os = new FileOutputStream(destFile)) {
            os.write(productImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return fileName;
    }

    @Override
    public void addProduct(ProductData productData, Brand brand, MultipartFile productImage) {
        try {
            String productImageName = saveProductImage(productImage);

            Product product = new Product();
            product.setName(productData.getName());
            product.setPetType(productData.getPetType());
            product.setCategory(productData.getCategory());
            product.setSubcategory(productData.getSubcategory());
            product.setBrand(brand);
            product.setPrice(productData.getPrice());
            product.setTaxAmount(productData.getTaxAmount());
            product.setStockQuantity(productData.getStockQuantity());
            product.setTaxAmount(25);
            product.setCurrentRating(BigDecimal.valueOf(0.0));
            product.setNumberOfBuyers(0);

            product.setProductImage(productImageName);

            productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException("Error saving product image", e);
        }
    }


    // Admin
    @Override
    public int getCurrentTaxAmount() {
        List<Product> productList = productRepository.findAll();

        if (!productList.isEmpty()) {
            Product firstProduct = productList.getFirst();
            return firstProduct.getTaxAmount();
        } else {
            return 0;
        }
    }

    @Override
    public void updateTaxAmount(int newTaxAmount) {
        List<Product> productList = productRepository.findAll();
        for (Product product : productList) {
            product.setTaxAmount(newTaxAmount);
        }
        productRepository.saveAll(productList);
    }

}