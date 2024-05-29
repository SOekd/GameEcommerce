package com.gameecommerce.backend.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Data
public class OrderCreateRequest {

    private Map<Long, Integer> productIds;

    @Size(min = 3, max = 90)
    private String playerName;

    @Nullable
    private String coupon;

}
