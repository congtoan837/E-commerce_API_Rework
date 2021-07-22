package com.poly.services.imp;

import com.poly.entity.User;
import com.poly.repositories.UserRepository;
import com.poly.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getByUser(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("Could not find User");
		}
		return new AuthService(user);
	}
}
