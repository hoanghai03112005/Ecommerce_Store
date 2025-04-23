package io.github.hoanghai03112005.repositories;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.hoanghai03112005.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
	Optional<User> findByVerificationToken(String token);
	Optional<User> findByResetToken(String token);
	
	 boolean existsByNameAndEmailNot(String name, String email);
	
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    boolean existsByPhone(String phone);
    
    @Query("SELECT u FROM User u WHERE u.enabled = false AND u.tokenExpiration < :now")
    List<User> findByEnabledFalseAndTokenExpirationBefore(@Param("now") LocalDateTime now);
    
    Optional<User> findByEmailAndEnabledIsTrueAndVerificationTokenIsNull(String email);
}
