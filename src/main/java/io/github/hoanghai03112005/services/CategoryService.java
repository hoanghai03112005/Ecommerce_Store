package io.github.hoanghai03112005.services;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.hoanghai03112005.EcommerceStoreApplication;
import io.github.hoanghai03112005.models.Category;
import io.github.hoanghai03112005.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
	private CategoryRepository categoryRepository;

    CategoryService(EcommerceStoreApplication ecommerceStoreApplication) {
    }
	
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}
	
	public Category getCategoryByID(Integer id) {
		return categoryRepository.findById(id).orElse(null);
	}
	
	public Category createCategory(Category category) {
		return categoryRepository.save(category);
	}
	
	@Transactional
	public Category updateCategory(Integer id, Category newCategory) {
		Optional<Category> existingCategory = categoryRepository.findById(id);
		if (existingCategory.isPresent()) {
			Category category = existingCategory.get();
			category.setName(newCategory.getName());
			category.setStatus(newCategory.isStatus());
			return categoryRepository.save(category);
		}
		return null;
	}
	
	public void deleteCategory(Integer id) {
		categoryRepository.deleteById(id);
	}
	
	public boolean existsByName(String name) {
		boolean exist = categoryRepository.existsByName(name);
		return exist;
	}
	
	public List<Category> getAllActiveCategory() {
		return categoryRepository.findAllByStatusTrue();
	}
}
