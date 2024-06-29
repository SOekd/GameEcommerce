package com.gameecommerce.backend.user.internal;

import com.gameecommerce.backend.user.User;
import com.gameecommerce.backend.user.UserRepository;
import com.gameecommerce.backend.user.UserService;
import com.gameecommerce.backend.user.exception.InvalidUserException;
import com.gameecommerce.backend.user.exception.UserAlreadyExistsException;
import com.gameecommerce.backend.user.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(@NotNull User user) {
        if (user.getId() != null)
            throw new InvalidUserException("User id must be null");

        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new UserAlreadyExistsException("User with username already exists");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User getUserByUsername(@NotNull String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found. Username: " + username));
    }

    @Override
    public User getUserById(@NotNull UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found. Id: " + id));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User deleteUserById(@NotNull UUID id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
        return user;
    }

    @Override
    public @Nullable UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        List.of()
                ))
                .orElse(null);
    }

}
