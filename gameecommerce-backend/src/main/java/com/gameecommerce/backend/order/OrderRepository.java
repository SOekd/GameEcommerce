package com.gameecommerce.backend.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findOrderByLink(String link);

    List<Order> findAllByOrderStageAndExpirationBefore(OrderStage orderStage, LocalDateTime expiration);

    List<Order> findAllByOrderStage(OrderStage orderStage);

}
