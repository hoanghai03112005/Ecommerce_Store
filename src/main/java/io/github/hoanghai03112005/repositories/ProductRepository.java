package io.github.hoanghai03112005.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.hoanghai03112005.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	boolean existsByName(String name);
	List<Product> findAllByStatusTrueOrderByIdDesc();

    List<Product> findAllByStatusTrueAndCategoryId(Long categoryId);
    List<Product> findAllByStatusTrueAndCategory_Name(String categoryName);
    List<Product> findAllByStatusTrueAndNameContainingIgnoreCase(String keyword);

             
}
