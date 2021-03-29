package com.fulfillmentBridge.FbSecurityService.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fulfillmentBridge.FbSecurityService.config.JWTutils;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	
	private AuthenticationManager authenticationManager;
	
	
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		System.out.println(username);
		System.out.println(password);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		//authenticate() launch the opertation of authentucation :call userDateilService and get user from db etc
		return authenticationManager.authenticate(authenticationToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication");
		//return the authetified user
		User user =(User) authResult.getPrincipal();
		
		//algoritm used to calculate the signature
		Algorithm algo = Algorithm.HMAC256(JWTutils.SECRET);
		//generate the jwt and put in it the claims(public and private(roles))
		String jwtAccessToken = JWT.create().withSubject(user.getUsername())
											.withExpiresAt(JWTutils.EXPIRATION)
											.withIssuer(request.getRequestURI().toString())
											.withClaim("roles", user.getAuthorities().stream().map(grantedAuth->grantedAuth.getAuthority()).collect(Collectors.toList()))
											.sign(algo);
		//send this jwt generated in header with name "Authorization"
		//response.setHeader("Authorization", jwtAccessToken);
		
		
		//generate the refreshJwt to avoid the revocation problem
				String jwtRefreshToken = JWT.create().withSubject(user.getUsername())
													.withExpiresAt(JWTutils.EXPIRATION_REFRESH)
													.withIssuer(request.getRequestURI().toString())
													.sign(algo);
				//send this jwt generated and the accesJwt in the response body in format Json
				Map<String, String> iDTokenMap = new HashMap<>();
				iDTokenMap.put("Access-token", jwtAccessToken);
				iDTokenMap.put("Refresh-token", jwtRefreshToken);
				new ObjectMapper().writeValue(response.getOutputStream(), iDTokenMap);
				//tell client the response may include json data
				response.setContentType("application/json");
				
	}

}
