package com.webshop.happy.paws.service;

import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public interface DiscountService {

    void applyDiscountToBrand(UUID brandId, int discountAmount);

    void removeDiscountFromBrand(UUID brandId);

    Integer getDiscountAmount(UUID brandId);

}
