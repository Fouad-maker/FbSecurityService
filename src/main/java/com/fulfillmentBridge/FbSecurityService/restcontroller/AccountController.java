package com.fulfillmentBridge.FbSecurityService.restcontroller;



import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fulfillmentBridge.FbSecurityService.config.JWTutils;
import com.fulfillmentBridge.FbSecurityService.entity.FbRole;
import com.fulfillmentBridge.FbSecurityService.entity.FbUser;
import com.fulfillmentBridge.FbSecurityService.entity.RoleUserForm;
import com.fulfillmentBridge.FbSecurityService.service.AccountService;


@RestController
public class AccountController {
	
	
	private AccountService accountService;
	
    public AccountController(AccountService accountService) {
    	this.accountService = accountService;
	}
    
    
    @GetMapping("/profile")
    public FbUser profile(Principal principal) {
    	return accountService.loadUserByUsername(principal.getName());
    }
	
	@GetMapping("/users")
	public List<FbUser> getFbUsers(){
		return accountService.listUsers();
	}
	
	@PostMapping("/users")
	public FbUser saveNewUser(@RequestBody FbUser user) {
		return accountService.addUser(user);
	}
	
	@PostMapping("/roles")
	public FbRole saveNewRole(@RequestBody FbRole role) {
		return accountService.addRole(role);
	}
	
	@PostMapping("/roleToUser")
	public void newRoleToUser(@RequestBody RoleUserForm roleUserForm) {
		 accountService.addRoleToUser(roleUserForm.getUsername(),roleUserForm.getRoleName());
	}
	
	@GetMapping("refreshToken")
	public void refreshToken(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String authToken = request.getHeader(JWTutils.HEADER_AUTH);
		if(authToken != null && authToken.startsWith(JWTutils.TOKEN_PREFIX)) {

			try {
				
				String jwt = authToken.substring(JWTutils.TOKEN_PREFIX.length());
				Algorithm algo=Algorithm.HMAC256(JWTutils.SECRET);
				//The verifier we use to verify the token
				JWTVerifier jwtVerifier = JWT.require(algo).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
				String username = decodedJWT.getSubject();
				FbUser fbUser = accountService.loadUserByUsername(username);
				String jwtAccessToken = JWT.create().withSubject(fbUser.getUsername())
						.withExpiresAt(JWTutils.EXPIRATION)
						.withIssuer(request.getRequestURI().toString())
						.withClaim("roles", fbUser.getFbRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList()))
						.sign(algo);
				
				Map<String, String> iDTokenMap = new HashMap<>();
				iDTokenMap.put("Access-token", jwtAccessToken);
				iDTokenMap.put("Refresh-token", jwt);
				new ObjectMapper().writeValue(response.getOutputStream(), iDTokenMap);
			} catch (Exception e) {
				throw e;
			}
		}else {
			throw new RuntimeException("required the refresh token!!!!");
		}
		
	}
	

}
