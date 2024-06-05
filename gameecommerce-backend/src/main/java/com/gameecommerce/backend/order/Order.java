package com.gameecommerce.backend.order;

import com.gameecommerce.backend.coupon.Coupon;
import com.gameecommerce.backend.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String link;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<OrderProduct> products;

    @Enumerated(EnumType.STRING)
    private OrderStage orderStage;

    private String playerName;

    @CreationTimestamp
    private LocalDateTime creation;

    private LocalDateTime expiration;

    @ManyToOne(fetch = FetchType.EAGER)
    private Coupon coupon;

    private String pixQrCode;

    // promotion codes
    @PositiveOrZero
    private int price;

    @PositiveOrZero
    private int discount;

    public int getTotalPrice() {
        return price - discount;
    }

}
