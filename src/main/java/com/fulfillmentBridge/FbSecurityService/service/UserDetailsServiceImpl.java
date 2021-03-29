package com.fulfillmentBridge.FbSecurityService.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fulfillmentBridge.FbSecurityService.entity.FbUser;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	
	private AccountService accountService;
	
	
	public UserDetailsServiceImpl(@Lazy AccountService accountService) {
	
		this.accountService = accountService;
	}


	
	
	
	//when user athentified use this method to load it's infos from a service we created
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		FbUser fbUser=accountService.loadUserByUsername(username);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		fbUser.getFbRoles().forEach(r->{
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
		});
		return  new User(fbUser.getUsername(),fbUser.getPassword(),authorities);
	}

}
