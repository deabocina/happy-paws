package com.webshop.happy.paws.controller;

import com.webshop.happy.paws.entity.Order;
import com.webshop.happy.paws.entity.OrderItem;
import com.webshop.happy.paws.entity.Product;
import com.webshop.happy.paws.entity.User;
import com.webshop.happy.paws.repository.OrderItemRepository;
import com.webshop.happy.paws.repository.OrderRepository;
import com.webshop.happy.paws.repository.ProductRepository;
import com.webshop.happy.paws.service.OrderService;
import com.webshop.happy.paws.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Controller
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private ProductRepository productRepository;


    @GetMapping("/shopping-bag")
    public String getShoppingBagPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);

        List<Product> productsInCart = orderService.getProductsInCart(session);
        model.addAttribute("productsInCart", productsInCart);

        // Provjerite postojanje mape productQuantities u sesiji
        if (session.getAttribute("productQuantities") == null) {
            Map<UUID, Integer> productQuantities = new HashMap<>();
            for (Product product : productsInCart) {
                productQuantities.put(product.getId(), 1);
            }
            session.setAttribute("productQuantities", productQuantities);
        }

        // Dobavite mapu productQuantities iz sesije
        Map<UUID, Integer> productQuantities = (Map<UUID, Integer>) session.getAttribute("productQuantities");
        model.addAttribute("productQuantities", productQuantities);

        BigDecimal total = orderService.calculateTotal(productsInCart);
        BigDecimal shippingCost = orderService.calculateShippingCost(total);
        BigDecimal totalOrder = total.add(shippingCost);

        session.setAttribute("copiedTotal", total);
        session.setAttribute("shippingCost", shippingCost);
        session.setAttribute("totalOrder", totalOrder);

        return "shopping-bag";
    }


    @PostMapping("/shopping-bag")
    public String updateShoppingBag(Model model, HttpSession session, @RequestParam("productId") UUID productId, @RequestParam("quantity") int quantity) {
        // Ako mapa productQuantities ne postoji u sesiji, stvaramo novu mapu
        Map<UUID, Integer> productQuantities = (Map<UUID, Integer>) session.getAttribute("productQuantities");
        if (productQuantities == null) {
            productQuantities = new HashMap<>();
        }

        productQuantities.put(productId, quantity);
        session.setAttribute("productQuantities", productQuantities);

        return "redirect:/shopping-bag";
    }


    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") UUID productId, HttpSession session, HttpServletRequest request) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            orderService.addToCart(product, session);
        }

        String referer = request.getHeader("Referer");

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/shopping-bag";
        }
    }

    @PostMapping("/remove-to-cart")
    public String removeToCart(@RequestParam("productId") UUID productId, HttpSession session) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            orderService.removeToCart(product, session);
        }
        return "redirect:/shopping-bag";
    }

    @GetMapping("/payment")
    public String getPaymentPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);


        List<Product> productsInCart = orderService.getProductsInCart(session);
        model.addAttribute("productsInCart", productsInCart);

        Map<UUID, Integer> productQuantities = (Map<UUID, Integer>) session.getAttribute("productQuantities");
        model.addAttribute("productQuantities", productQuantities);

        return "payment";
    }

    @GetMapping("/paypal")
    public String getPaymentConfirmationPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        return "paypal";
    }


    @PostMapping("/buy")
    public String processOrder(Model model, HttpSession session, @RequestParam("deliveryOption") String deliveryOption) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        List<Product> productsInCart = orderService.getProductsInCart(session);
        Map<UUID, Integer> productQuantities = (Map<UUID, Integer>) session.getAttribute("productQuantities");


        BigDecimal totalOrder = BigDecimal.ZERO;
        BigDecimal freeShippingThreshold = new BigDecimal("35");

        for (Product product : productsInCart) {
            BigDecimal pricePerItem = product.getPrice();
            BigDecimal taxAmount = BigDecimal.valueOf(product.getTaxAmount());
            int quantity = productQuantities.get(product.getId());
            BigDecimal totalPriceForProduct = pricePerItem.multiply(BigDecimal.valueOf(quantity));
            BigDecimal totalPriceWithTax = totalPriceForProduct.add(totalPriceForProduct.multiply(taxAmount.divide(BigDecimal.valueOf(100))));
            totalOrder = totalOrder.add(totalPriceWithTax);
        }

        BigDecimal shippingCost;
        if (totalOrder.compareTo(freeShippingThreshold) >= 0) {
            shippingCost = BigDecimal.ZERO;
        } else {
            shippingCost = new BigDecimal("4");
        }

        totalOrder = totalOrder.add(shippingCost);


        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUser(user);
        order.setTotalPrice(totalOrder);
        order.setDeliveryOption(deliveryOption);
        order.setOrderDate(new Date());
        order.setOrderStatus("Processing");

        orderRepository.save(order);
        UUID orderId = order.getId();
        session.setAttribute("deliveryOption", deliveryOption);
        session.setAttribute("orderId", orderId);
        session.setAttribute("totalOrder", totalOrder);

        for (Product product : productsInCart) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID());
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(productQuantities.get(product.getId()));

            BigDecimal pricePerItemWithTax = product.getPrice()
                    .multiply(BigDecimal.valueOf(1 + (product.getTaxAmount() / 100.0)))
                    .setScale(2, RoundingMode.HALF_UP);

            orderItem.setPrice(pricePerItemWithTax);
            orderItemRepository.save(orderItem);
        }


        // Ažuriranje zaliha i broja kupaca
        for (Product product : productsInCart) {
            int purchasedQuantity = productQuantities.get(product.getId());
            int updatedStockQuantity = product.getStockQuantity() - purchasedQuantity;
            int updatedBuyersCount = product.getNumberOfBuyers() + 1;

            product.setStockQuantity(updatedStockQuantity);
            product.setNumberOfBuyers(updatedBuyersCount);

            productService.saveProduct(product);
        }

        return "checkout";
    }


    @GetMapping("/checkout")
    public String getCheckoutPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);


        String deliveryOption = (String) session.getAttribute("deliveryOption");
        UUID orderId = (UUID) session.getAttribute("orderId");

        model.addAttribute("deliveryOption", deliveryOption);
        model.addAttribute("orderId", orderId);

        return "checkout";
    }

    @PostMapping("/clear-order")
    public String clearOrder(HttpSession session) {
//        session.removeAttribute("cart");
        session.removeAttribute("deliveryOption");
        session.removeAttribute("orderId");
        session.removeAttribute("productQuantities");
        session.removeAttribute("copiedTotal");
        session.removeAttribute("shippingCost");
        session.removeAttribute("totalOrder");

        return "redirect:/";
    }


    @GetMapping("/my-order")
    public String getUserOrderPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        int cartItemCount = orderService.getCartItemCount(session);
        model.addAttribute("cartItemCount", cartItemCount);

        List<Order> userOrders = orderRepository.findByUser(user);
        model.addAttribute("userOrders", userOrders);


        Map<UUID, List<Product>> orderProductsMap = new HashMap<>();
        for (Order order : userOrders) {
            List<Product> products = productService.getProductsByOrderId(order.getId());
            orderProductsMap.put(order.getId(), products);
        }
        model.addAttribute("orderProductsMap", orderProductsMap);

        return "my-order";
    }

    @PostMapping("/rate")
    public String rateProduct(@RequestParam UUID orderId, @RequestParam int rating) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getUserRating() != null) {
                return "Order already rated.";
            }
            if (rating < 1 || rating > 5) {
                return "Invalid rating. Please select a rating between 1 and 5.";
            }

            order.setUserRating(rating);
            orderRepository.save(order);

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

            for (OrderItem orderItem : orderItems) {
                Product product = orderItem.getProduct();
                if (product != null) {
                    // Dohvaćanje svih narudžbi koje sadrže ocjenu za trenutni proizvod
                    List<OrderItem> allOrderItems = orderItemRepository.findByProductId(product.getId());

                    double totalRating = 0.0;
                    int numberOfRatings = 0;
                    for (OrderItem item : allOrderItems) {
                        Order associatedOrder = item.getOrder();
                        if (associatedOrder.getUserRating() != null) {
                            totalRating += associatedOrder.getUserRating();
                            numberOfRatings++;
                        }
                    }

                    if (numberOfRatings > 0) {
                        double averageRating = totalRating / numberOfRatings;
                        product.setCurrentRating(BigDecimal.valueOf(averageRating).setScale(1, RoundingMode.HALF_UP));
                        productRepository.save(product);
                    }
                }
            }
            return "redirect:/my-order";
        }
        return "error";
    }


    // Staff
    @GetMapping("/order-management")
    public String getOrderManagementPage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);

        return "order-management";
    }

    @PostMapping("/update-order-status/{id}")
    public String updateOrderStatus(@PathVariable("id") UUID id, @RequestParam("orderStatus") String orderStatus, HttpSession session, Model model) {
        User user = (User)session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        return "redirect:/order-management";
    }

    @Transactional
    @PostMapping("/delete-order/{id}")
    public String deleteOrder(@PathVariable("id") UUID id) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderItemRepository.deleteAllByOrder(order);
            orderRepository.delete(order);
        }

        return "redirect:/order-management";
    }


}
