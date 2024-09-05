package com.webshop.happy.paws.service;

import com.webshop.happy.paws.entity.Product;
import com.webshop.happy.paws.entity.Wishlist;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface WishlistService {

    Wishlist getOrCreateWishlist(UUID userId);

    void addToWishlist(UUID userId, UUID productId);

    void removeFromWishlist(UUID userId, UUID productId);

    List<UUID> getProductIdsInWishlist(UUID userId);

    List<Product> getAllProductsInWishlist(UUID userId);

}
