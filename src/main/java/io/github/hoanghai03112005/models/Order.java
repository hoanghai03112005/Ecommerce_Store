package io.github.hoanghai03112005.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.github.hoanghai03112005.enums.Order_Status;
import io.github.hoanghai03112005.enums.Payment_method;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "total_price")
	private Double totalPrice;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
    private Payment_method paymentMethod = Payment_method.BANK;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Order_Status status = Order_Status.PENDING;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Một đơn hàng có nhiều sản phẩm
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Payment_method getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Payment_method paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public Order_Status getStatus() {
		return status;
	}

	public void setStatus(Order_Status status) {
		this.status = status;
	}
	
	
}
