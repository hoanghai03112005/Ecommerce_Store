package io.github.hoanghai03112005.controllers;

import java.io.IOException;

import java.security.Principal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.github.hoanghai03112005.EcommerceStoreApplication;
import io.github.hoanghai03112005.dtos.ProductDTO;
import io.github.hoanghai03112005.models.Category;
import io.github.hoanghai03112005.models.Order;
import io.github.hoanghai03112005.models.OrderItem;
import io.github.hoanghai03112005.models.Product;
import io.github.hoanghai03112005.models.User;
import io.github.hoanghai03112005.repositories.CategoryRepository;
import io.github.hoanghai03112005.repositories.ProductRepository;
import io.github.hoanghai03112005.repositories.UserRepository;
import io.github.hoanghai03112005.services.CategoryService;
import io.github.hoanghai03112005.services.OrderService;
import io.github.hoanghai03112005.services.ProductService;
import io.github.hoanghai03112005.services.UserCleanupService;
import io.github.hoanghai03112005.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final ProductService productService;

    private final UserRepository userRepository;

    private final UserCleanupService userCleanupService;

    private final EcommerceStoreApplication ecommerceStoreApplication;

    private final CategoryRepository categoryRepository;
    
    private final ProductRepository productRepository;
    
    private final OrderService orderService;
    
	@Autowired
	CategoryService categoryService;
	
	private final UserService userService;

    AdminViewController(OrderService orderService, ProductRepository productRepository ,CategoryRepository categoryRepository, EcommerceStoreApplication ecommerceStoreApplication,@Lazy UserService userService, UserCleanupService userCleanupService, UserRepository userRepository, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.ecommerceStoreApplication = ecommerceStoreApplication;
		this.userService = userService;
		this.productRepository = productRepository;
		this.userCleanupService = userCleanupService;
		this.userRepository = userRepository;
		this.productService = productService;
		this.orderService = orderService;
    }
	
    @ModelAttribute
    public void addUserToSession(HttpSession session, Principal principal) {
    	if(principal != null && session.getAttribute("currentLoggedInUserDetails") == null) {
    		String email = principal.getName();
    		User user = userService.getUserByEmail(email);
    		session.setAttribute("currentLoggedInUserDetails", user);
    	}
    }
    
	@GetMapping({"", "/"}) 
	public String adminIndex(HttpSession session, Principal principal) {
		if (principal != null) {
			String currentLoggedInUserEmail = principal.getName();
			User user = userService.getUserByEmail(currentLoggedInUserEmail);
			session.setAttribute("currentLoggedInUserDetails", user);
		}
		return "admin/AdminDashboard";
	}
	
	@GetMapping("/category")
	public String category(Model model, HttpSession session) {
		 System.out.println("User in session: " + session.getAttribute("currentLoggedInUserDetails"));
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		return "admin/category/category-home";
	}
	
	@GetMapping("/add-category")
	public String addCategory(Model model) {
		model.addAttribute("category", new Category());
		return "admin/category/category-add";
	}
	
	@PostMapping("/save-category")
	public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("category", category);
			return "admin/category/category-add";
		}
		
		if (categoryService.existsByName(category.getName())) {
			result.rejectValue("name", "error.category", "Tên danh mục đã tồn tại");
			model.addAttribute("category", category);
			return "admin/category/category-add";
		}
		
		categoryService.createCategory(category);
		
		return "redirect:/admin/category";
	}
	
	@GetMapping("/edit-category/{id}")
	public String editCategory(@PathVariable("id") int id, Model model) {
		
		Category categoryObj = categoryService.getCategoryByID(id);
		System.out.println(categoryObj.getId());
		model.addAttribute("category", categoryObj);
		return "admin/category/category-edit";
	}
	
	@PostMapping("/update-category")
	public String updateCategory(@RequestParam("id") Integer id,@Valid @ModelAttribute Category category, BindingResult result, Model model) {
		//TODO: process POST request
		if (result.hasErrors()) {
			model.addAttribute("category", category);
			model.addAttribute("id", id);
			return "admin/category/category-edit";
		}
		
		if (categoryService.existsByName(category.getName())) {
			result.rejectValue("name", "error.category", "Tên danh mục đã tồn tại");
			model.addAttribute("category", category);
			model.addAttribute("id", id);
			return "admin/category/category-edit";
		}
		
		 System.out.println("Updating category with ID: " + id);
		categoryService.updateCategory(id, category);
		return "redirect:/admin/category";
	}
	
	@GetMapping("/delete-category/{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {
		System.out.println("ID for delete: " + id);
		categoryService.deleteCategory(id);
		return "redirect:/admin/category";
	}
	
	@GetMapping("/users")
	public String showUsers(Model model) {
		List<User> users = userService.getAllUser();
		model.addAttribute("users", users);
		return "admin/users/user-home";
	}
	
	@PostMapping("/edit-user-status/{id}")
	public String changedUserStatus(@PathVariable Integer id, @RequestParam boolean status, RedirectAttributes redirectAttributes) {
		//TODO: process POST request
		User user = userService.getUserByID(id);
		if(user != null) {
			user.setEnabled(status);
			userRepository.save(user);
			redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");
		} else {
			 redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng");
		}
		return "redirect:/admin/users";
	}
	
	@GetMapping("/all-product")
	public String showProduct(Model model) {
		List<Product> products = productService.getAllProducts();
		model.addAttribute("products", products);
		return "admin/product/all-product";
	}
	
	@GetMapping("/add-product")
	public String getAddProduct(Model model) {
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		model.addAttribute("productDTO", new ProductDTO());
		return "admin/product/add-product";
	}
	
	@PostMapping("/save-product")
	public String postMethodName(@Valid @ModelAttribute("productDTO") ProductDTO productDTO, BindingResult result,
            @RequestParam("modelFile") MultipartFile modelFile,
            @RequestParam("image") MultipartFile imageFile,
            RedirectAttributes redirectAttributes, Model model) {
		//TODO: process POST request
		List<Category> categories = categoryService.getAllCategories();
		if (productService.existByName(productDTO.getName())) {
			model.addAttribute("categories", categories);
			result.rejectValue("name", "error.productDTO", "Tên sản phẩm đã tồn tại");
			model.addAttribute("productDTO", productDTO);
		}
		
		if (imageFile.isEmpty()) {
			model.addAttribute("categories", categories);
		    result.rejectValue("image", null, "Vui lòng chọn ảnh");
		}
		if (modelFile.isEmpty()) {
			model.addAttribute("categories", categories);
		    result.rejectValue("modelFile", null, "Vui lòng chọn file mô hình");
		}

		
		 if (result.hasErrors()) {
			 model.addAttribute("categories", categories);
			 return "admin/product/add-product"; 
		 }
		 
		try {
			productService.saveProduct(productDTO);
			
			redirectAttributes.addFlashAttribute("message", "Product uploaded successfully!");
			
			return "redirect:/admin/all-product";
		} catch (Exception e) {
			// TODO: handle exception
//			  redirectAttributes.addFlashAttribute("error", "Error occurred while saving the product: " + e.getMessage());
			  
			  return "redirect:/admin/add-product"; 
		}
		
	}
	
	
	@GetMapping("/edit-product/{id}")
	public String getMethodName(@PathVariable("id") Long id, Model model) {
		Product product = productService.getProductById(id);
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("product", product);
		model.addAttribute("categories" ,categories);
		return "admin/product/edit-product";
	}
	
	@PostMapping("/update-product")
	public String postMethodName(@RequestParam("id") Long id, @Valid @ModelAttribute ProductDTO productDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		//TODO: process POST request
		Product existingProduct = productService.findByID(id);
		List<Category> categories = categoryService.getAllCategories();
		try {
			if (!existingProduct.getName().equals(productDTO.getName()) && productRepository.existsByName(productDTO.getName())) {
			    model.addAttribute("product", existingProduct);
			    model.addAttribute("categories" ,categories);
			    redirectAttributes.addFlashAttribute("error", "Tên không được trùng lặp");
			    return "redirect:/admin/edit-product/" + id; // hoặc view tương ứng
			}

			
			productService.updateProduct(id, productDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/admin/all-product";
	}
	
	@GetMapping("/delete-product/{id}")
	public String getMethodName(@PathVariable("id") Long id) {
		productService.deleteProduct(id);
		return "redirect:/admin/all-product";
	}
	
	@GetMapping("/orders")
	public String orderView(Model model) {
		List<Order> orders = orderService.getAll();
		model.addAttribute("orders", orders);
		return "admin/orders/orders-home";
	}
	
	@GetMapping("/order-detail")
	public String showOrderDetail(@RequestParam("orderId") Long id, Model model) {
		Order order = orderService.findByID(id);
		List<OrderItem> items = order.getItems();
		NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(order.getTotalPrice());
		model.addAttribute("order" ,order);
		model.addAttribute("items" ,items);
		model.addAttribute("totalOrderPrice",formattedPrice);
		return "admin/orders/order-detail";
	}
	
}
