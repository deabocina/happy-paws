package com.webshop.happy.paws.controller;

import com.webshop.happy.paws.entity.Brand;
import com.webshop.happy.paws.repository.BrandRepository;
import com.webshop.happy.paws.service.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @Autowired
    private BrandRepository brandRepository;


    @PostMapping("/save-brand")
    public String saveBrand(@ModelAttribute("brand") Brand brand) {
        brandRepository.save(brand);
        return "redirect:/system-config";
    }

    @GetMapping("/system-config-brand-update")
    public String getBrandUpdate(Model model) {
        List<Brand> brands = brandRepository.findAll();
        model.addAttribute("brands", brands);
        model.addAttribute("selectedBrand", new Brand());
        return "system-config-brand-update";
    }

    @PostMapping("/update-brand")
    public String updateBrand(@RequestParam("selectedBrandId") UUID brandId, @RequestParam("brandName") String brandName) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new IllegalArgumentException("Invalid brand Id:" + brandId));
        brand.setName(brandName);
        brandRepository.save(brand);
        return "redirect:/system-config-brand-update";
    }

    @PostMapping("/delete-brand")
    public String deleteBrand(@RequestParam("brandId") UUID brandId) {
        brandRepository.deleteById(brandId);
        return "redirect:/system-config-brand-update";
    }


}
