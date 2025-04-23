package io.github.hoanghai03112005.controllers;

import java.io.IOException;
import java.security.Principal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.hoanghai03112005.dtos.UserLoginDTO;
import io.github.hoanghai03112005.dtos.UserRegistrationDTO;
import io.github.hoanghai03112005.enums.Payment_method;
import io.github.hoanghai03112005.models.Cart;
import io.github.hoanghai03112005.models.Category;
import io.github.hoanghai03112005.models.Order;
import io.github.hoanghai03112005.models.OrderItem;
import io.github.hoanghai03112005.models.Product;
import io.github.hoanghai03112005.models.User;
import io.github.hoanghai03112005.repositories.CategoryRepository;
import io.github.hoanghai03112005.repositories.ProductRepository;
import io.github.hoanghai03112005.repositories.UserRepository;
import io.github.hoanghai03112005.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@SessionAttributes("currentLoggedInUserDetails")
public class HomeViewController {

    private final ProductRepository productRepository;

    private final OrderService orderService;

    private final CategoryService categoryService;

    private final PasswordEncoder passwordEncoder;

    private final AdminViewController adminViewController;

    private final EmailService emailService;
    
    private final ProductService productService;

    private final UserRepository userRepository;

    private final UserService userService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    HomeViewController(ProductService productService, UserService userService, UserRepository userRepository, EmailService emailService, AdminViewController adminViewController, PasswordEncoder passwordEncoder, CategoryRepository categoryRepository, CategoryService categoryService, OrderService orderService, ProductRepository productRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.adminViewController = adminViewController;
        this.passwordEncoder = passwordEncoder;
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderService = orderService;
        this.productRepository = productRepository;
    }
	
    @ModelAttribute
    public void getUserDetails(Principal principal, Model model) {
    	if (principal != null) {
			String currentLoggedInUserEmail = principal.getName();
			User user = userService.getUserByEmail(currentLoggedInUserEmail);
			
			if (user != null) {
				model.addAttribute("currentLoggedInUserDetails", user);
				
//				Long countCartForUser = cartService.getCounterCart(currentUserDetails.getId());
//                model.addAttribute("countCartForUser", countCartForUser);
			}
		}
 	
//        List<Category> allActiveCategory = categoryService.findAllActiveCategory();
//        model.addAttribute("allActiveCategory", allActiveCategory);
    }
    
    
	@GetMapping("/")
	public String homeIndex(Model model) {
		List<Category> categories = categoryService.getAllActiveCategory();
		List<Product> latestEightActiveProducts = productService.getAllActiveProduct().stream()
			    .sorted((p1, p2) -> Long.compare(p2.getId(), p1.getId())) // id giảm dần
			    .limit(8)
			    .collect(Collectors.toList());

		model.addAttribute("categories" ,categories);
		model.addAttribute("latestEightActiveProducts" ,latestEightActiveProducts);
		return "index";
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@RequestParam String token, RedirectAttributes redirectAttributes) {
		Optional<User> optionalUser = userRepository.findByVerificationToken(token);
		
		if(optionalUser.isEmpty()) {
			 redirectAttributes.addFlashAttribute("error", "Token không hợp lệ");
		     return "redirect:/"; 
		}
		
		if(optionalUser.get().getTokenExpiration().isBefore(LocalDateTime.now())) {
			User user = optionalUser.get();
			userRepository.delete(user);
		    redirectAttributes.addFlashAttribute("error", "Token đã hết hạn, vui lòng đăng ký lại.");
		    return "redirect:/"; 
		}
		
		User user = optionalUser.get();
		user.setEnabled(true);
		user.setVerificationToken(null);
		userRepository.save(user);
		
		redirectAttributes.addFlashAttribute("message", "Xác thực thành công! Bạn có thể đăng nhập ngay.");
	    return "redirect:/login"; 
		
	}
	
	@GetMapping("/login")
	public String getLogin(Model model) {
		model.addAttribute("user", new UserLoginDTO());
		return "login";
	}
	
//	@PostMapping("/login") 
//	public String login(@ModelAttribute("user") UserLoginDTO userDTO, BindingResult result, Model model, HttpSession session) {
//		Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
//		
//		if (userOptional.isEmpty()) {
//	        result.rejectValue("email", "error.userDTO", "Tài khoản chưa xác nhận hoặc không tồn tại");
//	        model.addAttribute("user", userDTO);
//	        return "login";
//	    }
//
//	    User user = userOptional.get();
//	    
//	    if (!user.isEnabled()) {
//	        result.rejectValue("email", "error.userDTO", "Tài khoản chưa được kích hoạt");
//	        model.addAttribute("user", userDTO);
//	        return "login"; 
//	    }
//	    
//	    if (user.getVerificationToken() != null) {
//	        result.rejectValue("email", "error.userDTO", "Tài khoản chưa xác nhận email");
//	        model.addAttribute("user", userDTO);
//	        return "login"; 
//	    }
//		  try {
//			Authentication authentication = authenticationManager.authenticate(
//				     new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
//				);
//			
//			SecurityContextHolder.getContext().setAuthentication(authentication);
//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//			System.out.println("🔍 Logged in as: " + auth.getName());
//			System.out.println("🔍 Roles: " + auth.getAuthorities());
//
//			session.setAttribute("loggedInUser", user);
//
//	        return "redirect:/";
//		} catch (BadCredentialsException e) {
//			result.rejectValue("password", "error.userDTO", "Mật khẩu không chính xác");
//	        model.addAttribute("user", userDTO);
//	        return "login";
//		}
//	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new UserRegistrationDTO());
		return "register";
	}
	
	@PostMapping("/save-user")
	public String saveUser(@Valid @ModelAttribute("user") UserRegistrationDTO userDTO, BindingResult result, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
		//TODO: process POST request
		if (userRepository.existsByName(userDTO.getName())) {
			result.rejectValue("name", "error.userDTO", "Tên người dùng đã tồn tại");
			model.addAttribute("user", userDTO);
		}
		
		if (userService.existsByEmail(userDTO.getEmail())) {
			result.rejectValue("email", "error.userDTO", "Email đã tồn tại");
			model.addAttribute("user", userDTO);
		}
		
		if (userService.existsByPhone(userDTO.getPhone())) {
			result.rejectValue("phone", "error.userDTO", "Số điện thoại đã tồn tại");
			model.addAttribute("user", userDTO);
		}
		
		if (!userService.isPasswordMatch(userDTO.getPassword(), userDTO.getConfirmPassword())) {
		    result.rejectValue("confirmPassword", "error.userDTO", "Xác nhận mật khẩu không khớp");
		}
		
		if (result.hasErrors()) {
			model.addAttribute("user", userDTO);
			 return "register";
		} 
		
		 try {
		        userService.registerUser(userDTO);
		        redirectAttributes.addFlashAttribute("waitingForVerification", true);
		        redirectAttributes.addFlashAttribute("message", "Bạn đã đăng ký thành công! Vui lòng kiểm tra email để xác nhận tài khoản.");
		        return "redirect:/verify-pending"; 
		    } catch (IllegalArgumentException e) {// Lỗi logic (email trùng, password không khớp,...)
		        return "register";
		    } catch (IOException e) {
		        e.printStackTrace();
		        return "register";
		    }
	}
	
	@GetMapping("/verify-pending")
	public String showVerificationPendingPage(@ModelAttribute("waitingForVerification") Boolean waitingForVerification, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
	    
	    if (waitingForVerification == null || !waitingForVerification) {
	    	redirectAttributes.addFlashAttribute("message", "Xác thực thành công");
	        return "redirect:/login";
	    }

	    session.removeAttribute("waitingForVerification");

	    model.addAttribute("successMsg", "Bạn đã đăng ký thành công! Vui lòng kiểm tra email để xác nhận tài khoản.");
	    return "verify-pending"; 
	}
	
	@GetMapping("/forgot-password")
	public String getForgotPassword() {
		return "forgot-password";
	}
	
	@PostMapping("/forgot-password")
	public String processForgotPassword(@RequestParam("email") String email, Model model) {
		//TODO: process POST request
		User user = userService.getUserByEmail(email);
		
		 if (user == null) {
	          model.addAttribute("error", "Email không tồn tại trong hệ thống!");
	          return "forgot-password";
	     }
		 
		 emailService.sendResetPasswordEmail(user);
		 model.addAttribute("message", "Link xác nhận đã được gửi đến email (có hiệu lực trong 5p)");
	     return "forgot-password";
	}
	
	@GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        User user = userService.findByResetToken(token);

        if (user == null || user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token.");
            return "reset-password";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }
	
	@PostMapping("/reset-password")
	public String processResetPassword(@RequestParam("token") String token, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {
		//TODO: process POST request
		User user = userService.findByResetToken(token);
		
		if (user == null || user.getTokenExpiration().isBefore(LocalDateTime.now())) {
			redirectAttributes.addFlashAttribute("error", "Something wrong...");
            return "reset-password";
        }
		
		if (!password.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
            return "redirect:/reset-password?token=" + token;
        }
		
		user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null); 
        user.setTokenExpiration(null);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "Password reset successfully!");
        return "redirect:/login";
	}
	
	
	@GetMapping("/profile")
	public String showProfile(Model model) {
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);
		return "profile";
	}
	
	@GetMapping("/edit-profile")
	public String showEditProfile(Model model, Principal principal) {
		String email = principal.getName();
		User user = userService.getUserByEmail(email);
		model.addAttribute("user", user);
		return "edit-profile";
	}
	
	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute("user") User user, Principal principal, RedirectAttributes redirectAttributes, BindingResult result, Model model) {
		//TODO: process POST request
		String email = principal.getName();
		User existingUser = userService.getUserByEmail(email);
		 boolean hasError = false;
		
		
		if (!existingUser.getName().equals(user.getName()) && userService.existsByName(user.getName())) {
			result.rejectValue("name", "error.user", "Tên người dùng đã tồn tại");
			hasError = true;
		}
		if (!existingUser.getPhone().equals(user.getPhone()) && userService.existsByPhone(user.getPhone())) {
			result.rejectValue("phone", "error.user", "Số điện thoại đã tồn tại");
			hasError = true;
		}
		
		if (hasError) {
	        model.addAttribute("user", user);
	        return "edit-profile";
	    }
		
		
		userService.updateUser(email, user);
		redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");
		return "redirect:/profile";
	}
	
	@GetMapping("/products")
	public String getProducts(Model model, @RequestParam(value = "category", required = false) String categoryName, @RequestParam(value = "search", required = false) String keyword) {
		List<Category> categories = categoryService.getAllActiveCategory();
		List<Product> allActiveProducts;
		if (keyword != null && !keyword.isEmpty()) {
			allActiveProducts = productService.searchProduct(keyword);
			  model.addAttribute("searchValue", keyword);
		} else if(categoryName != null && !categoryName.equals("All")) {
			allActiveProducts = productService.getAllByStatusTrueAndCategory_Name(categoryName);
		} else {
			allActiveProducts = productService.getAllActiveProduct();
		}
		
		model.addAttribute("allActiveProducts", allActiveProducts);
		model.addAttribute("paramValue", categoryName);
		model.addAttribute("categories", categories);
		return "products";
	}
	
	@GetMapping("/product/{id}")
	public String showDetailProduct(Model model, @PathVariable("id") Long id) {
		Product product = productService.findByID(id);
		model.addAttribute("product" ,product);
		return "product-detail";
	}
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpSession session) {
		User user = (User) session.getAttribute("currentLoggedInUserDetails");
		if (user == null) {
			return "redirect:/login";
		}
		
		List<Cart> cartItems = cartService.getCartByUser(user);
	    model.addAttribute("carts", cartItems);
	    double totalOrderPrice = cartItems.stream()
	            .mapToDouble(cart -> cart.getProduct().getPrice())
	            .sum();
	        
	        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
	        String formattedPrice = formatter.format(totalOrderPrice);
	        model.addAttribute("totalOrderPrice", totalOrderPrice);
	        model.addAttribute("formattedTotalOrderPrice", formattedPrice);
	        session.setAttribute("formattedTotalOrderPrice", formattedPrice);

		return "cart";
	}
	
	@GetMapping("/add-to-cart")
	public String addToCart(@RequestParam Long productId, HttpSession session, RedirectAttributes redirectAttributes) {
	    User user = (User) session.getAttribute("currentLoggedInUserDetails");

	    if (user == null) {
	        return "redirect:/login";
	    }
	    
	    Product product = productService.getProductById(productId);
	    
	    cartService.addToCart(user, product);
	    redirectAttributes.addFlashAttribute("success", "Product added to cart!");
	    return "redirect:/products";
	}

	@GetMapping("/orders")
	public String showOrder(Model model, HttpSession session, @RequestParam(value = "productId", required = false) Long productId) {
		
		if (productId != null) {
	        // Mua 1 sản phẩm (Buy Now)
	        Product product = productService.findByID(productId);
	                           
	        
	        model.addAttribute("totalOrderPrice", product.getPrice());
	    } else {
	        // Mua qua giỏ hàng
	    	String totalOrderPrice = Optional.ofNullable((String) session.getAttribute("formattedTotalOrderPrice")).orElse("0");

			model.addAttribute("totalOrderPrice", totalOrderPrice);
	    }
		
		return "orders";
	}
	
	@GetMapping("/clear-cart")
	public String clearCart(HttpSession session) {
		  User user = (User) session.getAttribute("currentLoggedInUserDetails");
		cartService.clearCart(user);
		return "redirect:/cart";
	}
	
	@GetMapping("/delete-cart-product")
	public String deleteProductFromCart(@RequestParam("productId") Long productId, HttpSession session) {
		 User user = (User) session.getAttribute("currentLoggedInUserDetails");
		 cartService.removeFromCart(user, productService.findByID(productId));
		return "redirect:/cart";
	}
	
	@PostMapping("/place-order")
	public String placeOrder(@RequestParam("paymentMethod") Payment_method paymentMethod, @RequestParam(value = "productId", required = false) Long productId, HttpSession session, RedirectAttributes redirectAttribute) {
		//TODO: process POST request
		User user = (User) session.getAttribute("currentLoggedInUserDetails");
		Order order = new Order();
		System.out.println(productId);
		if (productId != null) {
	        // Mua lẻ
			
	        order = orderService.placeSingleOrder(user, productService.findByID(productId), paymentMethod);
	    } else {
	        // Đặt hàng từ giỏ
	    	order = orderService.placeOrder(user, paymentMethod);
	    }
		return "redirect:/checkout?orderId=" + order.getId();
	}
	
	@GetMapping("/checkout")
	public String showCheckOut(@RequestParam("orderId") Long orderId, Model model) {
		Order order = orderService.findByID(orderId); // Hoặc phương thức phù hợp
		NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(order.getTotalPrice());
        List<OrderItem> items = order.getItems();
        
	    model.addAttribute("order", order);
	    model.addAttribute("totalOrderPrice",formattedPrice);
	    model.addAttribute("items" ,items);
		return "checkout";
	}
	
	@GetMapping("/order-history")
	public String orderHistory(HttpSession session, Model model) {
		User user = (User) session.getAttribute("currentLoggedInUserDetails");
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Order> items = orderService.getOrderHistory(user);
        model.addAttribute("items" ,items);
        return "order-history";
	}
		
}
