package com.gameecommerce.backend.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Size(min = 4, max = 200)
    @NotNull
    private String name;

    @Size(min = 5, max = 4000)
    @NotNull
    private String description;

    // in cents
    @PositiveOrZero
    private double price;

    @ElementCollection(fetch = FetchType.EAGER)
    @NotNull
    private List<String> servers;

}
