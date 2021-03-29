package com.fulfillmentBridge.FbSecurityService.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulfillmentBridge.FbSecurityService.dao.FbRoleRepo;
import com.fulfillmentBridge.FbSecurityService.dao.FbUserRepo;
import com.fulfillmentBridge.FbSecurityService.entity.FbRole;
import com.fulfillmentBridge.FbSecurityService.entity.FbUser;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	private FbUserRepo fbUserRepo;
	private FbRoleRepo fbRoleRepo;
	private PasswordEncoder passwordEncoder;
	
	 public AccountServiceImpl( FbRoleRepo fbRoleRepo , FbUserRepo fbUserRepo , PasswordEncoder passwordEncoder ) {
		
		this.fbRoleRepo = fbRoleRepo;
		this.fbUserRepo = fbUserRepo;
		this.passwordEncoder = passwordEncoder;
		
	}

	@Override
	public FbUser addUser(FbUser user) {
		String pw = user.getPassword();
		user.setPassword(passwordEncoder.encode(pw));
		return fbUserRepo.save(user);
	}

	@Override
	public FbRole addRole(FbRole role) {
		
		return fbRoleRepo.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		FbUser appUser = fbUserRepo.findByUsername(username);
		FbRole appRole = fbRoleRepo.findByRoleName(roleName);
		appUser.getFbRoles().add(appRole);
		
	}

	@Override
	public FbUser loadUserByUsername(String username) {
		
		return fbUserRepo.findByUsername(username);
	}

	@Override
	public List<FbUser> listUsers() {
		
		return fbUserRepo.findAll();
	}

}
