package com.fulfillmentBridge.FbSecurityService.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fulfillmentBridge.FbSecurityService.filters.JwtAuthenticationFilter;
import com.fulfillmentBridge.FbSecurityService.filters.JwtAuthorizationFilter;
import com.fulfillmentBridge.FbSecurityService.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private UserDetailsServiceImpl userDetailsService;
	
	
	public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
		
		this.userDetailsService = userDetailsService;
	}


	/*
	 *  to specify the users allowed to access
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService);
	}
	
	
	
	
	/*
	 * specify the access policies
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//disable the generation of synchronized token used by SC to defend Cross-site request forgery(csrf) attacks
		//in case of statful auth it's not recomended to disable it
		http.csrf().disable();
		//tell spring we will use the statless auth
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//h2-db use the frames so SC have a default protection vs frames we need to disable it
		http.headers().frameOptions().disable();
		http.authorizeRequests().antMatchers("/h2-console/**","/refreshToken/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/users/**").hasAnyAuthority("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/users/**").hasAnyAuthority("ADMIN");
		//we don't need it statless auth 
		//http.formLogin();
		//permit all requests( default spring sec auth desactivated)
		http.authorizeRequests().anyRequest()./*permitAll()*/authenticated();
		//integrate the authenticationFilter created in the security config
		http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
		//add the authorizationFilter in the security config
		http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	
	
	
//  For BCrypt Encoded password
    @Bean
    public PasswordEncoder passwordEncoder() {
       PasswordEncoder encoder = new BCryptPasswordEncoder();
       return encoder;
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
    	// TODO Auto-generated method stub
    	return super.authenticationManagerBean();
    }

}
