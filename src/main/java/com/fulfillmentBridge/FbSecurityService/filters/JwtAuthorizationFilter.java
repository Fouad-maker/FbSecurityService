package com.fulfillmentBridge.FbSecurityService.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fulfillmentBridge.FbSecurityService.config.JWTutils;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	//this methode will executed with every request
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(request.getServletPath().equals("/refreshToken")) {
			filterChain.doFilter(request, response);
		}
		
		else {
			
			String jwtAuthorization=request.getHeader(JWTutils.HEADER_AUTH); 
			if (jwtAuthorization!=null && jwtAuthorization.startsWith(JWTutils.TOKEN_PREFIX)) {
			
			try {
				
				String jwt = jwtAuthorization.substring(JWTutils.TOKEN_PREFIX.length());
				Algorithm algo=Algorithm.HMAC256(JWTutils.SECRET);
				//The verifier we use to verify the token
				JWTVerifier jwtVerifier = JWT.require(algo).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
				String username = decodedJWT.getSubject();
				String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
				Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				for(String r:roles) {
					authorities.add(new SimpleGrantedAuthority(r));
				}
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(username,null,authorities);
				//authentify the user
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				//all is okey pass to the next Filter
				filterChain.doFilter(request, response);
				
			} catch (Exception e) {
				response.setHeader("err-msg", e.getMessage());
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
			
		} else {
			
			filterChain.doFilter(request, response);
			
		}
		}
	}
	

}
