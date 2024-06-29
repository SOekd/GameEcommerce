package com.gameecommerce.backend.security;

import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityService {

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    @Nullable UserDetails extractUser(String token);

    boolean verifyToken(String token);

}
