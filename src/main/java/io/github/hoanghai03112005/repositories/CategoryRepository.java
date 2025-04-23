package io.github.hoanghai03112005.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.hoanghai03112005.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	boolean existsByName(String name);
	List<Category> findAllByStatusTrue();
}
