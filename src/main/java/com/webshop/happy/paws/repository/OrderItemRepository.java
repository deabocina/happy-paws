package com.webshop.happy.paws.repository;

import com.webshop.happy.paws.entity.Order;
import com.webshop.happy.paws.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    List<OrderItem> findByOrderId(UUID orderId);

    List<OrderItem> findByProductId(UUID productId);

    void deleteAllByOrder(Order order);



}
