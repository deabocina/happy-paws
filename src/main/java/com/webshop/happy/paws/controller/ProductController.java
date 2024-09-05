package com.webshop.happy.paws.controller;

import com.webshop.happy.paws.data.ProductData;
import com.webshop.happy.paws.entity.*;
import com.webshop.happy.paws.repository.ProductRepository;
import com.webshop.happy.paws.service.*;
import com.webshop.happy.paws.utils.ValidationUtils;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final BrandService brandService;
    private final WishlistService wishlistService;
    private final OrderService orderService;
    private final DiscountService discountService;
    private final PetProfileService petProfileService;


    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/dog")
    public String getDogPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        List<Product> wishlistProducts = null;
        if (user != null) {
            wishlistProducts = wishlistService.getAllProductsInWishlist(user.getId());
        }
        model.addAttribute("wishlistProducts", wishlistProducts);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);

        List<Product> dogProducts = productService.getDogProducts();
        model.addAttribute("dogProducts", dogProducts);

        List<Brand> allBrands = brandService.getAllBrands();
        model.addAttribute("brands", allBrands);


        if (user != null) {
            List<PetProfile> petProfiles = petProfileService.getPetProfilesByUser(user);
            Date today = new Date();

            boolean discountApplied = petProfiles.stream()
                    .anyMatch(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today));

            if (discountApplied) {
                PetProfile birthdayPet = petProfiles.stream()
                        .filter(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today))
                        .findFirst()
                        .orElse(null);

                if (birthdayPet != null) {
                    model.addAttribute("birthdayDiscountPercent", 25);
                    model.addAttribute("birthdayPetName", birthdayPet.getPetName());
                    model.addAttribute("birthdayPetType", birthdayPet.getPetType());
                }
            } else {
                model.addAttribute("discountRemovedMessage", "Birthday discounts have been removed for pets whose birthday has passed.");
            }
        }

        // Brand Discount
        Map<UUID, Integer> brandDiscounts = new HashMap<>();
        for (Brand brand : allBrands) {
            Integer discountAmount = discountService.getDiscountAmount(brand.getId());
            brandDiscounts.put(brand.getId(), discountAmount);
        }
        model.addAttribute("brandDiscounts", brandDiscounts);

        return "dog";
    }


    @GetMapping("/cat")
    public String getCatPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        List<Product> wishlistProducts = null;
        if (user != null) {
            wishlistProducts = wishlistService.getAllProductsInWishlist(user.getId());
        }
        model.addAttribute("wishlistProducts", wishlistProducts);

        List<Product> catProducts = productService.getCatProducts();
        List<Brand> allBrands = brandService.getAllBrands();
        model.addAttribute("catProducts", catProducts);
        model.addAttribute("brands", allBrands);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);


        if (user != null) {
            List<PetProfile> petProfiles = petProfileService.getPetProfilesByUser(user);
            Date today = new Date();

            boolean discountApplied = petProfiles.stream()
                    .anyMatch(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today));

            if (discountApplied) {
                PetProfile birthdayPet = petProfiles.stream()
                        .filter(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today))
                        .findFirst()
                        .orElse(null);

                if (birthdayPet != null) {
                    model.addAttribute("birthdayDiscountPercent", 25);
                    model.addAttribute("birthdayPetName", birthdayPet.getPetName());
                    model.addAttribute("birthdayPetType", birthdayPet.getPetType());
                }
            } else {
                model.addAttribute("discountRemovedMessage", "Birthday discounts have been removed for pets whose birthday has passed.");
            }
        }

        List<Brand> brands = brandService.getAllBrands();
        Map<UUID, Integer> brandDiscounts = new HashMap<>();
        for (Brand brand : brands) {
            Integer discountAmount = discountService.getDiscountAmount(brand.getId());
            brandDiscounts.put(brand.getId(), discountAmount);
        }
        model.addAttribute("brandDiscounts", brandDiscounts);


        return "cat";
    }

    @GetMapping("/small-animal")
    public String getSmallAnimalPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        List<Product> wishlistProducts = null;
        if (user != null) {
            wishlistProducts = wishlistService.getAllProductsInWishlist(user.getId());
        }
        model.addAttribute("wishlistProducts", wishlistProducts);

        List<Product> smallAnimalProducts = productService.getSmallAnimalProducts();
        List<Brand> allBrands = brandService.getAllBrands();
        model.addAttribute("smallAnimalProducts", smallAnimalProducts);
        model.addAttribute("brands", allBrands);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);


        if (user != null) {
            List<PetProfile> petProfiles = petProfileService.getPetProfilesByUser(user);
            Date today = new Date();

            boolean discountApplied = petProfiles.stream()
                    .anyMatch(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today));

            if (discountApplied) {
                PetProfile birthdayPet = petProfiles.stream()
                        .filter(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today))
                        .findFirst()
                        .orElse(null);

                if (birthdayPet != null) {
                    model.addAttribute("birthdayDiscountPercent", 25);
                    model.addAttribute("birthdayPetName", birthdayPet.getPetName());
                    model.addAttribute("birthdayPetType", birthdayPet.getPetType());
                }
            } else {
                model.addAttribute("discountRemovedMessage", "Birthday discounts have been removed for pets whose birthday has passed.");
            }
        }


        List<Brand> brands = brandService.getAllBrands();
        Map<UUID, Integer> brandDiscounts = new HashMap<>();
        for (Brand brand : brands) {
            Integer discountAmount = discountService.getDiscountAmount(brand.getId());
            brandDiscounts.put(brand.getId(), discountAmount);
        }
        model.addAttribute("brandDiscounts", brandDiscounts);


        return "small-animal";
    }

    @GetMapping("/pet_product")
    public String filterPetProducts(
            @RequestParam(name = "petType") String petType,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "subcategory", required = false) String subcategory,
            @RequestParam(name = "brandName", required = false) String brandName,
            Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        Brand brand = null;
        if (brandName != null && !brandName.isEmpty()) {
            brand = brandService.getBrandByName(brandName);
        }

        List<Product> filteredProducts = productService.getFilteredProducts(petType, category, subcategory, brand);
        model.addAttribute("filteredProducts", filteredProducts);
        model.addAttribute("categories", productService.getAllProducts().stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList()));

        model.addAttribute("brands", brandService.getAllBrands());
        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);


        if (user != null) {
            List<PetProfile> petProfiles = petProfileService.getPetProfilesByUser(user);
            Date today = new Date();

            boolean discountApplied = petProfiles.stream()
                    .anyMatch(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today));

            if (discountApplied) {
                PetProfile birthdayPet = petProfiles.stream()
                        .filter(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today))
                        .findFirst()
                        .orElse(null);

                if (birthdayPet != null) {
                    model.addAttribute("birthdayDiscountPercent", 25);
                    model.addAttribute("birthdayPetName", birthdayPet.getPetName());
                    model.addAttribute("birthdayPetType", birthdayPet.getPetType());
                }
            } else {
                model.addAttribute("discountRemovedMessage", "Birthday discounts have been removed for pets whose birthday has passed.");
            }
        }


        List<Brand> allBrands = brandService.getAllBrands();
        Map<UUID, Integer> brandDiscounts = new HashMap<>();
        for (Brand brands : allBrands) {
            Integer discountAmount = discountService.getDiscountAmount(brands.getId());
            brandDiscounts.put(brands.getId(), discountAmount);
        }
        model.addAttribute("brandDiscounts", brandDiscounts);


        if ("dog".equalsIgnoreCase(petType)) {
            return "dog";
        } else if ("cat".equalsIgnoreCase(petType)) {
            return "cat";
        } else if ("small-animal".equalsIgnoreCase(petType)) {
            return "small-animal";
        } else {
            return "error";
        }
    }

    @GetMapping("/")
    public String getHomePage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);


        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);
        return "home";
    }


    @GetMapping("/search")
    public String searchProducts(@RequestParam(name = "query", required = false) String name,
                                 Model model, HttpSession session) {

        List<Product> searchResults = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            searchResults = productService.getSearchProducts(name);
        }

        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        model.addAttribute("searchResults", searchResults);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);


        if (user != null) {
            List<PetProfile> petProfiles = petProfileService.getPetProfilesByUser(user);
            Date today = new Date();

            boolean discountApplied = petProfiles.stream()
                    .anyMatch(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today));

            if (discountApplied) {
                PetProfile birthdayPet = petProfiles.stream()
                        .filter(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today))
                        .findFirst()
                        .orElse(null);

                if (birthdayPet != null) {
                    model.addAttribute("birthdayDiscountPercent", 25);
                    model.addAttribute("birthdayPetName", birthdayPet.getPetName());
                    model.addAttribute("birthdayPetType", birthdayPet.getPetType());
                }
            } else {
                model.addAttribute("discountRemovedMessage", "Birthday discounts have been removed for pets whose birthday has passed.");
            }
        }


        // Brand Discount
        List<Brand> brands = brandService.getAllBrands();
        Map<UUID, Integer> brandDiscounts = new HashMap<>();
        for (Brand brand : brands) {
            Integer discountAmount = discountService.getDiscountAmount(brand.getId());
            brandDiscounts.put(brand.getId(), discountAmount);
        }
        model.addAttribute("brandDiscounts", brandDiscounts);


        return "search-result";
    }


    @GetMapping("/location")
    public String getLocationPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);
        return "location";
    }

    @GetMapping("/about-us")
    public String getAboutUsPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);
        return "about-us";
    }

    @GetMapping("/shipping")
    public String getShippingPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);
        return "shipping";
    }

    @GetMapping("/payment-options")
    public String getPaymentOptionsPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);
        return "payment-options";
    }

    // Staff
    @GetMapping("/product-management")
    public String getProductManagementPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        return "product-management";
    }

    @GetMapping("/product-management-add")
    public String getProductManagementAddPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        List<Brand> brands = brandService.getAllBrands();
        model.addAttribute("brands", brands);

        List<String> petTypes = Arrays.asList("Dog", "Cat", "Small Animal");
        model.addAttribute("petTypes", petTypes);
        List<String> categories = Arrays.asList("Food", "Treats", "Toys");
        model.addAttribute("categories", categories);
        List<String> subcategories = Arrays.asList("Dry food", "Wet food", "Dental treats", "Regular treats");
        model.addAttribute("subcategories", subcategories);

        return "product-management-add";
    }


    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute ProductData productData, @RequestParam(value = "brandId") UUID brandId,
                             @RequestParam(value = "productImage", required = false) MultipartFile productImage, HttpSession session) {
        Brand brand = brandService.getBrandById(brandId);
        if (brand == null) {
            throw new IllegalArgumentException("Invalid brand ID");
        }

        productService.addProduct(productData, brand, productImage);

        return "redirect:/product-management";
    }


    @GetMapping("/update-product/{id}")
    public String getUpdateProductPage(@PathVariable("id") UUID id, Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("petTypes", Arrays.asList("Dog", "Cat", "Small-Animal"));
        model.addAttribute("categories", Arrays.asList("Food", "Treats", "Toys"));
        model.addAttribute("subcategories", Arrays.asList("Dry food", "Wet food", "Dental treats", "Regular treats"));


        model.addAttribute("selectedBrandId", product.getBrand().getId());
        model.addAttribute("selectedPetType", product.getPetType());
        model.addAttribute("selectedCategory", product.getCategory());
        model.addAttribute("selectedSubcategory", product.getSubcategory());

        return "product-management-update";
    }


    @PostMapping("/update-product/{id}")
    public String updateProduct(@PathVariable("id") UUID id,
                                @ModelAttribute ProductData productData,
                                @RequestParam("brandId") UUID brandId,
                                @RequestParam("petType") String petType,
                                @RequestParam("category") String category,
                                @RequestParam("subcategory") String subcategory,
                                @RequestParam("productImage") MultipartFile productImage) {

        Brand brand = brandService.getBrandById(brandId);
        if (brand == null) {
            throw new IllegalArgumentException("Invalid brand ID");
        }

        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            throw new IllegalArgumentException("Invalid product ID");
        }

        existingProduct.setName(productData.getName());
        existingProduct.setPetType(petType);
        existingProduct.setCategory(category);
        existingProduct.setSubcategory(subcategory);
        existingProduct.setBrand(brand);
        existingProduct.setPrice(productData.getPrice());
        existingProduct.setTaxAmount(25);
        existingProduct.setStockQuantity(productData.getStockQuantity());

        try {
            if (!productImage.isEmpty()) {
                String productImageName = productService.saveProductImage(productImage);
                existingProduct.setProductImage(productImageName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving product image", e);
        }

        productService.saveProduct(existingProduct);

        return "redirect:/product-management";
    }


    @PostMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            productRepository.delete(product);
        }

        return "redirect:/product-management";
    }


    @GetMapping("/system-config")
    public String getSystemConfigPage(Model model) {
        int currentTaxAmount = productService.getCurrentTaxAmount();
        model.addAttribute("currentTaxAmount", currentTaxAmount);
        model.addAttribute("brand", new Brand());

        return "system-config";
    }


    @PostMapping("/update-tax")
    public String updateTaxAmount(@RequestParam("taxAmount") int newTaxAmount) {
        productService.updateTaxAmount(newTaxAmount);
        return "redirect:/system-config";
    }


}