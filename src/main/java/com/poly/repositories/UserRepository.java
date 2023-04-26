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

	Page<User> findByNameContainingOrUsernameContaining(String keyword, Pageable pageable);

	default Page<User> getUserByNameOrUsername(String keyword, Pageable pageable) {
		if (keyword == null || keyword.isEmpty()) {
			return findAll(pageable);
		}
		return findByNameContainingOrUsernameContaining(keyword, pageable);
	}

	User getByVerifyCode(String verifyCode);

	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.enabled = true WHERE u.verifyCode = :code")
	void findByVerifyCodeAndEnable(String code);
}
