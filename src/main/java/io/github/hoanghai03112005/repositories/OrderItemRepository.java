package io.github.hoanghai03112005.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.hoanghai03112005.models.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
