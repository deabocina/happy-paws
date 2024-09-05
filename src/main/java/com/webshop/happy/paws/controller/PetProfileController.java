package com.webshop.happy.paws.controller;

import com.webshop.happy.paws.data.PetProfileData;
import com.webshop.happy.paws.entity.PetProfile;
import com.webshop.happy.paws.entity.User;
import com.webshop.happy.paws.repository.PetProfileRepository;
import com.webshop.happy.paws.service.DiscountService;
import com.webshop.happy.paws.service.PetProfileService;
import com.webshop.happy.paws.service.OrderService;
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


@Controller
@AllArgsConstructor
public class PetProfileController {

    private final PetProfileService petProfileService;
    private final OrderService orderService;
    private final DiscountService discountService;

    @Autowired
    private PetProfileRepository petProfileRepository;


    @GetMapping("/pet-profile")
    public String getPetProfilePage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);

        List<PetProfile> petProfiles = petProfileService.getPetProfilesByUser(user);
        model.addAttribute("petProfiles", petProfiles);


        Date today = new Date();

        boolean discountApplied = petProfiles.stream()
                .anyMatch(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today));

        if (discountApplied) {
            PetProfile birthdayPet = petProfiles.stream()
                    .filter(petProfile -> ValidationUtils.isSameDayAndMonth(petProfile.getBirthDate(), today))
                    .findFirst()
                    .orElse(null);

            if (birthdayPet != null) {
                model.addAttribute("birthdayPetName", birthdayPet.getPetName());
                model.addAttribute("birthdayPetType", birthdayPet.getPetType());
            }
        } else {
            model.addAttribute("discountRemovedMessage", "Birthday discounts have been removed for pets whose birthday has passed.");
        }

        return "pet-profile";
    }


    @GetMapping("/pet-profile-add")
    public String getPetProfileAddPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);

        return "pet-profile-add";
    }

    @GetMapping("/pet-profile-update/{id}")
    public String getPetProfileUpdatePage(@PathVariable("id") UUID id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);

        Optional<PetProfile> optionalPetProfile = petProfileRepository.findById(id);
        if (optionalPetProfile.isPresent()) {
            PetProfile petProfile = optionalPetProfile.get();
            model.addAttribute("petProfile", petProfile);
        }

        return "pet-profile-update";
    }

    @PostMapping("/add-pet-profile")
    public String addPetProfile(@ModelAttribute PetProfileData petProfileData, @RequestParam(value = "petImage", required = false) MultipartFile petImage, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        List<PetProfile> userPetProfiles = petProfileRepository.findByUser(user);
        if (userPetProfiles.size() >= 3) {
            return "redirect:/pet-profile";
        }

        petProfileData.setUser(user);
        petProfileService.addPetProfile(petProfileData, petImage);

        return "redirect:/pet-profile";
    }

    @PostMapping("/update-pet-profile/{id}")
    public String updatePetProfile(@PathVariable("id") UUID id, @ModelAttribute PetProfileData updatedPetProfileData, @RequestParam(value = "petImage", required = false) MultipartFile petImage) {
        Optional<PetProfile> optionalPetProfile = petProfileRepository.findById(id);

        if (optionalPetProfile.isPresent()) {
            PetProfile existingPetProfile = optionalPetProfile.get();

            existingPetProfile.setPetName(updatedPetProfileData.getPetName());
            existingPetProfile.setPetType(updatedPetProfileData.getPetType());
            existingPetProfile.setBirthDate(updatedPetProfileData.getBirthDate());

            if (petImage != null && !petImage.isEmpty()) {
                try {
                    String petImageName = petProfileService.savePetImage(petImage);
                    existingPetProfile.setPetImage(petImageName);
                } catch (IOException e) {
                    throw new RuntimeException("Error saving pet image", e);
                }
            }

            petProfileRepository.save(existingPetProfile);
        }

        return "redirect:/pet-profile";
    }

    @PostMapping("/delete-pet-profile/{id}")
    public String deletePetProfile(@PathVariable("id") UUID id) {
        Optional<PetProfile> optionalPetProfile = petProfileRepository.findById(id);

        if (optionalPetProfile.isPresent()) {
            PetProfile petProfile = optionalPetProfile.get();
            petProfileRepository.delete(petProfile);
        }

        return "redirect:/pet-profile";
    }

}