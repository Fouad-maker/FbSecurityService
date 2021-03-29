package com.fulfillmentBridge.FbSecurityService.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fulfillmentBridge.FbSecurityService.entity.FbUser;

public interface FbUserRepo extends JpaRepository<FbUser, Long> {
	
	FbUser findByUsername(String username);

}
