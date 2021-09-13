package com.poly.repositories;

import com.poly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	User getById(UUID id);

	User getByUsername(String username);

	@Query("SELECT u FROM User AS u WHERE " +
			"u.name LIKE %:user% OR " +
			"u.email LIKE %:user% OR " +
			"u.phone LIKE %:user% OR " +
			"u.address LIKE %:user% OR " +
			"u.username LIKE %:user%")
	Page<User> getAllUser(String user, Pageable pageable);

	User getByVerifyCode(String verifyCode);

	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.enabled = true WHERE u.verifyCode = :code")
	void findByVerifyCodeAndEnable(String code);
}
