package com.webshop.happy.paws.service.implementation;

import com.webshop.happy.paws.repository.OrderRepository;
import com.webshop.happy.paws.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.webshop.happy.paws.entity.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public void addToCart(Product product, HttpSession session) {
        List<Product> productsInCart = (List<Product>) session.getAttribute("cart");
        if (productsInCart == null) {
            productsInCart = new ArrayList<>();
        }
        productsInCart.add(product);
        session.setAttribute("cart", productsInCart);
    }

    @Override
    public void removeToCart(Product product, HttpSession session) {
        List<Product> productsInCart = (List<Product>) session.getAttribute("cart");
        if (productsInCart == null) {
            productsInCart = new ArrayList<>();
        }
        productsInCart.remove(product);
        session.setAttribute("cart", productsInCart);
    }

    @Override
    public List<Product> getProductsInCart(HttpSession session) {
        List<Product> productsInCart = (List<Product>) session.getAttribute("cart");
        return productsInCart != null ? productsInCart : new ArrayList<>();
    }

    @Override
    public int getCartItemCount(HttpSession session) {
        List<Product> productsInCart = (List<Product>) session.getAttribute("cart");
        return productsInCart != null ? productsInCart.size() : 0;
    }

    @Override
    public BigDecimal calculateTotal(List<Product> products) {
        BigDecimal total = BigDecimal.ZERO;
        for (Product product : products) {
            BigDecimal taxAmount = BigDecimal.valueOf(product.getTaxAmount());
            BigDecimal priceWithTax = product.getPrice().add(product.getPrice().multiply(taxAmount.divide(BigDecimal.valueOf(100))));
            total = total.add(priceWithTax);
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateShippingCost(BigDecimal total) {
        BigDecimal shippingCost;
        if (total.compareTo(BigDecimal.valueOf(35)) >= 0) {
            shippingCost = BigDecimal.ZERO;
        } else {
            shippingCost = BigDecimal.valueOf(4);
        }
        return shippingCost.setScale(2, RoundingMode.HALF_UP);
    }

}
