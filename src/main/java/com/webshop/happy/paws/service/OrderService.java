package com.webshop.happy.paws.service;

import com.webshop.happy.paws.entity.Order;
import com.webshop.happy.paws.entity.Product;
import com.webshop.happy.paws.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {

    void addToCart(Product product, HttpSession session);

    void removeToCart(Product product, HttpSession session);

    List<Product> getProductsInCart(HttpSession session);

    int getCartItemCount(HttpSession session);

    BigDecimal calculateTotal(List<Product> products);

    BigDecimal calculateShippingCost(BigDecimal total);

}
