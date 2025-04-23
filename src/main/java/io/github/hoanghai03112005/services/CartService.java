package io.github.hoanghai03112005.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.hoanghai03112005.models.Cart;
import io.github.hoanghai03112005.models.Product;
import io.github.hoanghai03112005.models.User;
import io.github.hoanghai03112005.repositories.CartRepository;
import jakarta.transaction.Transactional;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;
	
	public List<Cart> getCartByUser(User user) {
		return cartRepository.findByUser(user);
	}
	
	public void addToCart(User user, Product product) {
		Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);
		if(existingCart.isEmpty()) {
			Cart cart = new Cart();
			cart.setUser(user);
			cart.setProduct(product);
			cartRepository.save(cart);
		}
	}
	
	@Transactional
	public void removeFromCart(User user, Product product) {
		cartRepository.deleteByUserAndProduct(user, product);
	}
	
	public void clearCart(User user) {
		List<Cart> cartItems = cartRepository.findByUser(user);
        cartRepository.deleteAll(cartItems);
	}
	
	public int countCartByUser(User user) {
		return cartRepository.countByUser(user);
	}
}
