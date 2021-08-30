package com.poly.services;

import com.poly.entity.Role;
import com.poly.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AuthService implements UserDetails {
	
	private User users;

	private Collection<? extends GrantedAuthority> roles;

	public AuthService(User user, Collection<? extends GrantedAuthority> roles) {
		super();
		this.users = user;
		this.roles = roles;
	}

	public static AuthService build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
				new SimpleGrantedAuthority(role.getName().name())
		).collect(Collectors.toList());

		return new AuthService(user, authorities);
	}

	public Long getId() {
		return users.getId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return users.getPassword();
	}

	@Override
	public String getUsername() {
		return users.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
