package com.gameecommerce.backend.product;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ProductService {

    Product create(@NotNull Product product);

    Product update(@NotNull Long id, @NotNull Product product);

    Product getById(@NotNull Long id);

    Product deleteById(@NotNull Long id);

    List<Product> getAll();

}
