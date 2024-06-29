package com.gameecommerce.backend;

import com.gameecommerce.backend.user.User;
import com.gameecommerce.backend.user.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GameEcommerceStartup {

    private final UserService userService;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {

        val user = new User(
                null,
                "admin",
                "admin"
        );

        userService.create(user);
    }

}
