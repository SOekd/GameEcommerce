package com.gameecommerce.backend.product.internal;

import com.gameecommerce.backend.product.Product;
import com.gameecommerce.backend.product.ProductRepository;
import com.gameecommerce.backend.product.ProductService;
import com.gameecommerce.backend.product.exception.InvalidProductException;
import com.gameecommerce.backend.product.exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product create(@NotNull Product product) {
        if (product.getId() != null) {
            throw new InvalidProductException("Product id must be null");
        }

        return productRepository.save(product);
    }

    @Override
    public Product update(@NotNull Long id, @NotNull Product product) {
        var existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found. Id: " + id));

        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setServers(product.getServers());
        existingProduct.setName(product.getName());

        return productRepository.save(existingProduct);
    }

    @Override
    public Product getById(@NotNull Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found. Id: " + id));
    }

    @Override
    public Product deleteById(@NotNull Long id) {
        val product = getById(id);
        productRepository.deleteById(id);
        return product;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

}
