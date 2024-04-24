package com.gameecommerce.backend.user.internal;

import com.gameecommerce.backend.user.User;
import com.gameecommerce.backend.user.UserRepository;
import com.gameecommerce.backend.user.UserService;
import com.gameecommerce.backend.user.exception.InvalidUserException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(@NotNull User user) {

        if (user.getId() != null)
            throw new InvalidUserException("User id must be null");

        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidUserException("User name must not be null or blank");
        }

        if (userRepository.findById(user.getId()).isPresent())
            throw new InvalidUserException("User with id %s already exists".formatted(user.getId()));

        return userRepository.save(user);
    }

}
