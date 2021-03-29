package com.fulfillmentBridge.FbSecurityService.service;

import java.util.List;

import com.fulfillmentBridge.FbSecurityService.entity.FbRole;
import com.fulfillmentBridge.FbSecurityService.entity.FbUser;

public interface AccountService {
	
	FbUser addUser(FbUser user);
	FbRole addRole(FbRole role);
	void addRoleToUser(String userName , String roleName);
	FbUser loadUserByUsername(String userName);
	List<FbUser> listUsers();

}
