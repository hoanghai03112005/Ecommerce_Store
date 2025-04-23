package io.github.hoanghai03112005.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.hoanghai03112005.enums.Payment_method;
import io.github.hoanghai03112005.models.Cart;
import io.github.hoanghai03112005.models.Order;
import io.github.hoanghai03112005.models.OrderItem;
import io.github.hoanghai03112005.models.Product;
import io.github.hoanghai03112005.models.User;
import io.github.hoanghai03112005.repositories.CartRepository;
import io.github.hoanghai03112005.repositories.OrderItemRepository;
import io.github.hoanghai03112005.repositories.OrderRepository;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	public Order findByID(Long id) {
		return orderRepository.findById(id).orElse(null);
	}
	
	public List<Order> getAll() {
		return orderRepository.findAll();
	}
	
	public List<Order> getOrderHistory(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }
	
	public Order placeOrder(User user, Payment_method paymentMethod) {
		List<Cart> cartItems = cartRepository.findByUser(user);
		
		if(cartItems.isEmpty()) {
			return null;
		}
		
		double total = cartItems.stream()
			    .map(item -> item.getProduct().getPrice().doubleValue())
			    .reduce(0.0, Double::sum);


		Order order = new Order();
		order.setUser(user);
		order.setPaymentMethod(paymentMethod);
		order.setTotalPrice(total);
		order = orderRepository.save(order);
		
	    for (Cart cart : cartItems) {
			OrderItem item = new OrderItem();
			item.setOrder(order);
			item.setPrice(cart.getProduct().getPrice());
			item.setProduct(cart.getProduct());
			orderItemRepository.save(item);
		}        
	    
	    cartRepository.deleteAll(cartItems);
	    return order;
	}
	
	@Transactional
	public Order placeSingleOrder(User user, Product product, Payment_method paymentMethod) {
		
		
		Order order = new Order();
	    order.setUser(user);
	    order.setTotalPrice(product.getPrice());
	    order.setPaymentMethod(paymentMethod);
	    order = orderRepository.save(order);

	    
	    
	    OrderItem item = new OrderItem();
	    item.setOrder(order);
	    item.setProduct(product);
	    item.setPrice(product.getPrice());
	    orderItemRepository.save(item);
	    
	    return order;
	}
}
