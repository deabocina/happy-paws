package com.webshop.happy.paws.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "discount", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "discount_amount", nullable = false)
    private Integer discountAmount;

    @ManyToOne
    @JoinColumn(name = "pet_profile_id")
    private PetProfile petProfile;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "original_price", nullable = false, precision = 6, scale = 2)
    private BigDecimal originalPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
