package com.gameecommerce.backend.order;

import com.gameecommerce.backend.cupon.Coupon;
import com.gameecommerce.backend.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    private OrderStage orderStage;

    private String playerName;

    @CreationTimestamp
    private LocalDateTime creation;

    @ManyToOne
    private Coupon coupon;

    // promotion codes
    private int price;

    private int quantity;

    private int total;

}
