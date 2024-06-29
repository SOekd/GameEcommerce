package com.gameecommerce.backend.order;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Data
public class OrderCreateRequest {

    private Map<Long, Integer> products;

    @Size(min = 3, max = 90)
    private String playerName;

}
