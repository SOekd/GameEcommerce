package com.gameecommerce.backend.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class RandomUtils {

    private final SecureRandom RANDOM = new SecureRandom();

    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String getRandomString(int minimum, int maximum) {
        if (minimum < 0 || maximum < 0) {
            throw new IllegalArgumentException("Minimum and maximum must be non-negative");
        }

        if (minimum > maximum) {
            throw new IllegalArgumentException("Minimum must be less than or equal to maximum");
        }

        int length = RANDOM.nextInt(maximum - minimum + 1) + minimum;
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return result.toString();
    }

}
