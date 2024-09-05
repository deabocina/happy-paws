package com.webshop.happy.paws.service.implementation;

import com.webshop.happy.paws.repository.OrderItemRepository;
import com.webshop.happy.paws.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository categoryRepository;

}
