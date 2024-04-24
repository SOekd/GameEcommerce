package com.gameecommerce.backend.history;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface HistoryRepository extends CrudRepository<History, UUID> {
}
