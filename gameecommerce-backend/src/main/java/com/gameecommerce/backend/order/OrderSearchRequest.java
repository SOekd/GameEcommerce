package com.gameecommerce.backend.order;

import lombok.*;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSearchRequest {

    private String search;

    private int page;

    private int itemsPerPage;

    private String sortColumn;

    private Sort.Direction sortDirection;

    private List<OrderState> states;

}
