package com.gameecommerce.backend.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    @Min(4)
    @Max(200)
    private String name;

    @Column(length = 4000)
    @Min(5)
    private String description;

    // in cents
    @Min(1000)
    private int price;

    @PositiveOrZero
    private int stock;

    private byte[] image;

    @Nullable
    private String gameServer;

    @ElementCollection
    private List<String> commands;

}
