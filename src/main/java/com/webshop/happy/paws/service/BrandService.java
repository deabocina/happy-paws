package com.webshop.happy.paws.service;

import com.webshop.happy.paws.entity.Brand;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public interface BrandService {

    Brand getBrandByName(String name);

    Brand getBrandById(UUID id);

    List<Brand> getAllBrands();

}
