package com.gameecommerce.backend.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findOrderByLink(String link);

    List<Order> findAllByStateAndExpirationBefore(OrderState state, LocalDateTime expiration);

    List<Order> findAllByState(OrderState state);

    List<Order> findAllByStateIn(List<OrderState> states, Pageable pageable);

    long countAllByStateIn(List<OrderState> stages);

    List<Order> findAllByPlayerNameLikeAndStateIn(String playerName, List<OrderState> states, Pageable pageable);

    long countAllByPlayerNameLikeAndStateIn(String playerName, List<OrderState> states);

}
