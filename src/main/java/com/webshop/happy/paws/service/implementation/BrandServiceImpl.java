package com.webshop.happy.paws.service.implementation;

import com.webshop.happy.paws.entity.Brand;
import com.webshop.happy.paws.repository.BrandRepository;
import com.webshop.happy.paws.service.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public Brand getBrandByName(String name) {
        return brandRepository.findByName(name).orElse(null);
    }

    @Override
    public Brand getBrandById(UUID id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

}
