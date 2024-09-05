package com.webshop.happy.paws.controller;

import com.webshop.happy.paws.data.UserData;
import com.webshop.happy.paws.entity.User;
import com.webshop.happy.paws.repository.UserRepository;
import com.webshop.happy.paws.service.OrderService;
import com.webshop.happy.paws.service.ProductService;
import com.webshop.happy.paws.service.UserService;
import com.webshop.happy.paws.utils.PasswordUtils;
import com.webshop.happy.paws.utils.ValidationUtils;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String getRegisterPage(Model model, HttpSession session) {
        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("registerRequest", new User());

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserData request, @RequestParam String confirmPassword, Model model, HttpSession session) {
        try {
            if (!request.getPassword().equals(confirmPassword)) {
                throw new RuntimeException("Passwords do not match");
            }
            request.setRole("User");

            Optional<User> existingUserOptional = userRepository.findByEmail(request.getEmail());
            if (existingUserOptional.isPresent()) {
                throw new RuntimeException("Email already exists");
            }

            User registeredUser = userService.register(request);
            if (registeredUser == null) {
                return "error";
            }
            session.setAttribute("loggedInUser", registeredUser);
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            return "register";
        }
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpSession session) {
        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("loginRequest", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model,  @RequestParam String password, @RequestParam String email, HttpSession session) {
        try {
            user.setPassword(password);
            user.setEmail(email);
            User authenticatedUser = userService.authentication(user.getEmail(), user.getPassword());

            if (authenticatedUser != null) {
                session.setAttribute("loggedInUser", authenticatedUser);
                if (authenticatedUser.getRole().equals("Admin")) {
                    return "redirect:/admin-home";
                } else if (authenticatedUser.getRole().equals("Staff")) {
                    return "redirect:/staff-home";
                }
                return "redirect:/";
            } else {
                throw new RuntimeException("Authentication failed");
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/user-page")
    public String getUserPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);
        return "user-page";
    }

    @PostMapping("/update-user-profile")
    public String updateProfile(@ModelAttribute User updatedUser, HttpSession session) {
        User loggedInUser = (User)session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            if (!ValidationUtils.isStringNullOrEmpty(updatedUser.getEmail())) {
                loggedInUser.setEmail(updatedUser.getEmail());
            }
            if (!ValidationUtils.isStringNullOrEmpty(updatedUser.getPhoneNumber())) {
                loggedInUser.setPhoneNumber(updatedUser.getPhoneNumber());
            }
            if (!ValidationUtils.isStringNullOrEmpty(updatedUser.getCity())) {
                loggedInUser.setCity(updatedUser.getCity());
            }
            if (!ValidationUtils.isStringNullOrEmpty(updatedUser.getAddress())) {
                loggedInUser.setAddress(updatedUser.getAddress());
            }
            if (!ValidationUtils.isStringNullOrEmpty(updatedUser.getPassword())) {
                loggedInUser.setPassword(PasswordUtils.hashPassword(updatedUser.getPassword()));
            }
            userRepository.save(loggedInUser);
        }
        return "redirect:/user-page";
    }

    @PostMapping("/delete-user-profile")
    public String deleteProfile(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            userRepository.delete(loggedInUser);
            session.invalidate();
        }
        return "redirect:/";
    }


    // Staff
    @GetMapping("/staff-home")
    public String getStaffHomePage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        return "staff-home";
    }


    // Admin
    @GetMapping("/admin-home")
    public String getAdminHomePage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        return "admin-home";
    }

    @GetMapping("/user-management")
    public String getUserManagementPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());

        return "user-management";
    }


    @GetMapping("/user-management-add")
    public String getUserManagementAddPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        model.addAttribute("registerRequest", new User());

        return "user-management-add";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute UserData request, @RequestParam String confirmPassword, Model model, HttpSession session) {
        try {
            if (!request.getPassword().equals(confirmPassword)) {
                throw new RuntimeException("Passwords do not match");
            }
            User registeredUser = userService.register(request);
            if (registeredUser == null) {
                return "error";
            }
            session.setAttribute("loggedInUser", registeredUser);
            return "redirect:/user-management";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            return "user-management-add";
        }
    }

    @GetMapping("/update-user/{id}")
    public String getUpdateUserPage(@PathVariable("id") UUID id, Model model) {
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(id));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            return "user-management-update";
        }
        return "redirect:/user-management";
    }


    @PostMapping("/update-user/{id}")
    public String updateUser(@PathVariable("id") UUID id, @ModelAttribute User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();

            userToUpdate.setName(updatedUser.getName());
            userToUpdate.setSurname(updatedUser.getSurname());
            userToUpdate.setRole(updatedUser.getRole());
            userToUpdate.setGender(updatedUser.getGender());
            userToUpdate.setDateOfBirth(updatedUser.getDateOfBirth());
            userToUpdate.setEmail(updatedUser.getEmail());
            userToUpdate.setPhoneNumber(updatedUser.getPhoneNumber());
            userToUpdate.setCity(updatedUser.getCity());
            userToUpdate.setAddress(updatedUser.getAddress());

            if (!ValidationUtils.isStringNullOrEmpty(updatedUser.getPassword())) {
                userToUpdate.setPassword(PasswordUtils.hashPassword(updatedUser.getPassword()));
            }

            userRepository.save(userToUpdate);
        }

        return "redirect:/user-management";
    }


    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable("id") UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);
        }

        return "redirect:/user-management";
    }


}
