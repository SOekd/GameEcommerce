package com.gameecommerce.backend.order;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody @Valid OrderCreateRequest request) {
        return ResponseEntity.ok(orderService.create(request));
    }

    @GetMapping("/{link}")
    public ResponseEntity<Order> findById(@PathVariable String link) {
        return ResponseEntity.ok(orderService.getOrderByLink(link));
    }
    @GetMapping("/delivery")
    public ResponseEntity<List<Order>> getAllDelivery() {
        return ResponseEntity.ok(orderService.getAllToDeliver());
    }


}
