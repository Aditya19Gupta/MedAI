package com.medai.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.medai.entities.User;
import com.medai.repo.UserRepo;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepo repo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=repo.getUserByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("User not found..");
		}
		
		UserCustomDetails userCustomDetails = new UserCustomDetails(user);
		return userCustomDetails;
	}

}
