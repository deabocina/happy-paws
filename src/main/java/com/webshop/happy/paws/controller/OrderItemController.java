package com.webshop.happy.paws.controller;

import com.webshop.happy.paws.repository.OrderItemRepository;
import com.webshop.happy.paws.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class OrderItemController {

    private final OrderItemService categoryService;

    @Autowired
    private OrderItemRepository categoryRepository;

}
