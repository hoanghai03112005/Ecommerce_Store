package io.github.hoanghai03112005.global;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import io.github.hoanghai03112005.models.Category;
import io.github.hoanghai03112005.models.User;
import io.github.hoanghai03112005.services.CartService;
import io.github.hoanghai03112005.services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
    private CartService cartService;
	
	@ModelAttribute("allActiveCategory")
	public List<Category> populateCategories() {
		return categoryService.getAllActiveCategory();
	}
	
	@ModelAttribute("countCartForUser")
	public int getCartItemCount(HttpServletRequest session) {
		User user = (User) session.getSession().getAttribute("currentLoggedInUserDetails");
		if (user != null) {
            return cartService.countCartByUser(user);
        }
        return 0;

	}
}
