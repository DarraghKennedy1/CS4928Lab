package com.cafepos.domain;

import java.util.Optional;

/**
 * Repository interface for Order persistence.
 * Part of the Domain layer - defines the contract but not the implementation.
 */
public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(long id);
}
