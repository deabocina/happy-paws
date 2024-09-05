package com.webshop.happy.paws.controller;

import com.webshop.happy.paws.entity.Product;
import com.webshop.happy.paws.entity.User;
import com.webshop.happy.paws.repository.WishlistRepository;
import com.webshop.happy.paws.service.OrderService;
import com.webshop.happy.paws.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
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
public class WishlistController {

    private final WishlistService wishlistService;
    private final OrderService orderService;

    @Autowired
    private WishlistRepository wishlistRepository;

    @GetMapping("/wishlist")
    public String getWishlistPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        List<Product> wishlistProducts = wishlistService.getAllProductsInWishlist(user.getId());
        model.addAttribute("wishlistProducts", wishlistProducts);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);
        return "wishlist";
    }


    @PostMapping("/add-to-wishlist")
    public String addToWishlist(@RequestParam UUID productId, HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user != null) {
            wishlistService.addToWishlist(user.getId(), productId);
        } else {
            return "redirect:/login";
        }

        String referer = request.getHeader("Referer");

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/remove-from-wishlist")
    public String removeFromWishlist(@RequestParam UUID productId, HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user != null) {
            wishlistService.removeFromWishlist(user.getId(), productId);
        } else {
            return "redirect:/login";
        }

        String referer = request.getHeader("Referer");

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/wishlist";
        }
    }



}
