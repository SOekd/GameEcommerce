package com.gameecommerce.backend.history;

import com.gameecommerce.backend.item.Item;
import com.gameecommerce.backend.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Item item;

    @ManyToOne
    private User buyer;

    @CreationTimestamp
    private LocalDateTime purchaseDate;

    // promotion codes
    private int price;

    private int quantity;

    private int total;

}
