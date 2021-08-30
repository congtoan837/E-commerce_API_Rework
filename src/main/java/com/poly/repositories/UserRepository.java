package com.poly.repositories;

import com.poly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User getById(Long id);

	User getByUsername(String username);

	@Query("SELECT u FROM User AS u WHERE " +
			"u.name LIKE %:user% OR " +
			"u.email LIKE %:user% OR " +
			"u.phone LIKE %:user% OR " +
			"u.address LIKE %:user% OR " +
			"u.username LIKE %:user%")
	Page<User> getAllUser(String user, Pageable pageable);
}
