package com.fulfillmentBridge.FbSecurityService.config;

import java.util.Date;

public class JWTutils {
	public static final String SECRET="Fbsecret";
	public static final String HEADER_AUTH="Authorization";
	public static final String TOKEN_PREFIX="Bearer ";
	public static final Date EXPIRATION=new Date(System.currentTimeMillis()+1*60*1000);
	public static final Date EXPIRATION_REFRESH=new Date(System.currentTimeMillis()+120*60*1000);

	
	
	

}
