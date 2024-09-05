package com.webshop.happy.paws.service.implementation;

import com.webshop.happy.paws.entity.Brand;
import com.webshop.happy.paws.entity.Discount;
import com.webshop.happy.paws.entity.Product;
import com.webshop.happy.paws.repository.BrandRepository;
import com.webshop.happy.paws.repository.DiscountRepository;
import com.webshop.happy.paws.repository.ProductRepository;
import com.webshop.happy.paws.service.DiscountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@AllArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    @Override
    public void applyDiscountToBrand(UUID brandId, int discountAmount) {
        List<Product> products = productRepository.findByBrandId(brandId);
        BigDecimal discountFactor = BigDecimal.ONE.subtract(BigDecimal.valueOf(discountAmount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new RuntimeException("Brand not found"));

        for (Product product : products) {
            Discount discount = new Discount();
            discount.setBrand(brand);
            discount.setDiscountAmount(discountAmount);
            discount.setOriginalPrice(product.getPrice());
            discount.setProduct(product);
            discountRepository.save(discount);

            BigDecimal discountedPrice = product.getPrice().multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
            product.setPrice(discountedPrice);
            productRepository.save(product);
        }
    }

    @Override
    public void removeDiscountFromBrand(UUID brandId) {
        List<Discount> discounts = discountRepository.findByBrandId(brandId);
        List<Product> products = productRepository.findByBrandId(brandId);

        Map<UUID, BigDecimal> originalPrices = new HashMap<>();

        for (Discount discount : discounts) {
            Product product = discount.getProduct();
            if (product != null && product.getBrand().getId().equals(brandId)) {
                originalPrices.put(product.getId(), discount.getOriginalPrice());
            }
        }

        for (Product product : products) {
            BigDecimal originalPrice = originalPrices.get(product.getId());
            if (originalPrice != null) {
                product.setPrice(originalPrice);
                productRepository.save(product);
            }
        }

        discountRepository.deleteAll(discounts);
    }


    @Override
    public Integer getDiscountAmount(UUID brandId) {
        Discount discount = discountRepository.findFirstByBrandId(brandId);
        if (discount != null) {
            return discount.getDiscountAmount();
        } else {
            return 0;
        }
    }


}
