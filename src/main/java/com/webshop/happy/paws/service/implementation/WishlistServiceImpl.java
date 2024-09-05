package com.webshop.happy.paws.service.implementation;

import com.webshop.happy.paws.entity.Product;
import com.webshop.happy.paws.entity.User;
import com.webshop.happy.paws.entity.Wishlist;
import com.webshop.happy.paws.repository.WishlistRepository;
import com.webshop.happy.paws.service.ProductService;
import com.webshop.happy.paws.service.UserService;
import com.webshop.happy.paws.service.WishlistService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;
    private final UserService userService;


    @Override
    public Wishlist getOrCreateWishlist(UUID userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);

        if (!wishlists.isEmpty()) {
            return wishlists.getFirst();
        } else {
            Wishlist wishlist = new Wishlist();
            User user = userService.getUserById(userId);
            wishlist.setUser(user);
            return wishlistRepository.save(wishlist);
        }
    }


    @Override
    public void addToWishlist(UUID userId, UUID productId) {
        Wishlist wishlist = new Wishlist();

        User user = userService.getUserById(userId);
        wishlist.setUser(user);

        Product product = productService.getProductById(productId);
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);
    }

    @Override
    public void removeFromWishlist(UUID userId, UUID productId) {
        List<Wishlist> userWishlists = wishlistRepository.findByUserId(userId);

        for (Wishlist wishlist : userWishlists) {
            if (wishlist.getProduct() != null && wishlist.getProduct().getId().equals(productId)) {
                wishlistRepository.delete(wishlist);
                break;
            }
        }
    }

    @Override
    public List<UUID> getProductIdsInWishlist(UUID wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElse(null);
        if (wishlist != null) {
            List<UUID> productIds = new ArrayList<>();
            if (wishlist.getProduct() != null) {
                productIds.add(wishlist.getProduct().getId());
            }
            return productIds;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Product> getAllProductsInWishlist(UUID userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        List<Product> wishlistProducts = new ArrayList<>();

        for (Wishlist wishlist : wishlists) {
            if (wishlist.getProduct() != null) {
                wishlistProducts.add(wishlist.getProduct());
            }
        }

        return wishlistProducts;
    }


}
