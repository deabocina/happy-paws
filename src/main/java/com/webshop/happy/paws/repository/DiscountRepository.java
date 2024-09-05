package com.webshop.happy.paws.repository;

import com.webshop.happy.paws.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    List<Discount> findByBrandId(UUID brandId);

    Discount findFirstByBrandId(UUID brandId);

}
