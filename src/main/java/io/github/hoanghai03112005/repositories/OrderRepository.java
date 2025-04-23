package io.github.hoanghai03112005.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.hoanghai03112005.models.Order;
import io.github.hoanghai03112005.models.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	List<Order> findByUserOrderByCreatedAtDesc(User user);
}
