package com.gameecommerce.backend.order;

import com.gameecommerce.backend.gateway.PaymentGateway;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderProduct> products;

    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.PRIVATE)
    private OrderState state;

    private String playerName;

    @CreationTimestamp
    private LocalDateTime creation;

    private LocalDateTime expiration;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PaymentGateway paymentGateway;

    @PositiveOrZero
    private double price;

    public void changeState(OrderState state) {
        this.state = state;
    }

}
