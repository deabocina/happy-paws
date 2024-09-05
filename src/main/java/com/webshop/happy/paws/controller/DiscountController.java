package com.webshop.happy.paws.controller;

import com.webshop.happy.paws.entity.Brand;
import com.webshop.happy.paws.entity.User;
import com.webshop.happy.paws.repository.DiscountRepository;
import com.webshop.happy.paws.service.BrandService;
import com.webshop.happy.paws.service.DiscountService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class DiscountController {

    private final DiscountService discountService;
    private final BrandService brandService;

    @Autowired
    private DiscountRepository discountRepository;


    @GetMapping("/discount-management")
    public String showDiscountManagementPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        List <Brand> brand = brandService.getAllBrands();

        model.addAttribute("user", user);
        model.addAttribute("brands", brand);

        return "discount-management";
    }

    @PostMapping("/apply-discount")
    public String applyDiscount(HttpSession session, @RequestParam UUID brandId, @RequestParam int discountAmount, Model model) {
        User user = (User)session.getAttribute("loggedInUser");

        try {
            discountService.applyDiscountToBrand(brandId, discountAmount);
            model.addAttribute("successMessage", "Discount applied successfully to all products of the brand.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error applying discount: " + e.getMessage());
        }
        model.addAttribute("user", user);

        return "redirect:/discount-management";
    }

    @PostMapping("/remove-discount")
    public String removeDiscount(HttpSession session, @RequestParam UUID brandId, Model model) {
        User user = (User)session.getAttribute("loggedInUser");

        try {
            discountService.removeDiscountFromBrand(brandId);
            model.addAttribute("successMessage", "Discount removed successfully from all products of the brand.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error removing discount: " + e.getMessage());
        }
        model.addAttribute("user", user);

        return "redirect:/discount-management";
    }

}
