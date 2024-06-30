package com.gameecommerce.backend.order;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/search")
    public ResponseEntity<OrderSearchResponse> search(@RequestBody OrderSearchRequest request) {
        return ResponseEntity.ok(orderService.search(request));
    }

    @PutMapping("/deliver/{id}")
    public ResponseEntity<Order> deliver(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.deliver(id));
    }

}
