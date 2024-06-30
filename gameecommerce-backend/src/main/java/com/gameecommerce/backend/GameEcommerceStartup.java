package com.gameecommerce.backend;

import com.gameecommerce.backend.order.OrderCreateRequest;
import com.gameecommerce.backend.order.OrderService;
import com.gameecommerce.backend.product.Product;
import com.gameecommerce.backend.product.ProductService;
import com.gameecommerce.backend.user.User;
import com.gameecommerce.backend.user.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class GameEcommerceStartup {

    private final UserService userService;

    private final ProductService productService;

    private final OrderService orderService;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {

        val user = new User(
                null,
                "admin",
                "admin"
        );

        userService.create(user);

        var product = new Product(
                null,
                "produto-teste",
                "Uma breve descrição",
                1,
                List.of()
        );

        product = productService.create(product);

        orderService.create(new OrderCreateRequest(
                Map.of(product.getId(), 1),
                "contateste123"
        ));

    }

}
