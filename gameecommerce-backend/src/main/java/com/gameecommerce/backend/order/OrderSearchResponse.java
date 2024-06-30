package com.gameecommerce.backend.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSearchResponse {

    private List<Order> orders;

    private long total;

}
