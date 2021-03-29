package com.fulfillmentBridge.FbSecurityService.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fulfillmentBridge.FbSecurityService.entity.FbRole;

public interface FbRoleRepo extends JpaRepository<FbRole, Long>{
	
	FbRole findByRoleName(String roleName);

}
