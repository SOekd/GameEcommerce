package com.gameecommerce.backend.user;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User create(@NotNull User user);

    User getUserByEmail(@NotNull String email);

    User getUserByUsername(@NotNull String username);

    User getUserById(@NotNull UUID id);

    List<User> getAll();

}
