package io.github.hoanghai03112005.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.hoanghai03112005.models.Cart;
import io.github.hoanghai03112005.models.Product;
import io.github.hoanghai03112005.models.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	List<Cart> findByUser(User user);
	
	 Optional<Cart> findByUserAndProduct(User user, Product product);
	 
	 void deleteByUserAndProduct(User user, Product product);
	 
	 int countByUser(User user);
}
