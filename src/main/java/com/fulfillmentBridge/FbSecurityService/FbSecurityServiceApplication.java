package com.fulfillmentBridge.FbSecurityService;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fulfillmentBridge.FbSecurityService.entity.FbRole;
import com.fulfillmentBridge.FbSecurityService.entity.FbUser;
import com.fulfillmentBridge.FbSecurityService.service.AccountService;

@SpringBootApplication
public class FbSecurityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FbSecurityServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(AccountService accountService) {
		return args -> {
			 accountService.addRole(new FbRole(null,"USER"));
			 accountService.addRole(new FbRole(null,"ADMIN"));
			 accountService.addRole(new FbRole(null,"CUSTOMER_MANAGER"));
			 accountService.addRole(new FbRole(null,"PRODUCT_MANAGER"));
			 accountService.addRole(new FbRole(null,"FACTURE_MANAGER"));
			 
			 accountService.addUser(new FbUser(null , "user1","1234",new ArrayList<>()));
			 accountService.addUser(new FbUser(null , "user2","1234",new ArrayList<>()));
			 accountService.addUser(new FbUser(null , "admin","1234",new ArrayList<>()));
			 accountService.addUser(new FbUser(null , "user3","1234",new ArrayList<>()));
			 accountService.addUser(new FbUser(null , "user4","1234",new ArrayList<>()));
			 
			 accountService.addRoleToUser("user1", "USER");
			 accountService.addRoleToUser("admin", "ADMIN");
			 accountService.addRoleToUser("user2", "CUSTOMER_MANAGER");
			 accountService.addRoleToUser("user2", "USER");
			 accountService.addRoleToUser("user3", "USER");
			 accountService.addRoleToUser("user3", "PRODUCT_MANAGER");
			 accountService.addRoleToUser("user4", "USER");
			 accountService.addRoleToUser("user4", "FACTURE_MANAGER");


			 


			
		};
		
	}
}
