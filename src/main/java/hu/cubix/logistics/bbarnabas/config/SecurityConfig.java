package hu.cubix.logistics.bbarnabas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;

import hu.cubix.logistics.bbarnabas.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		
		UserBuilder users = User.builder(); 
		
		UserDetails addressManager = users
			.username("addressManager")
			.password(passwordEncoder.encode("pass"))
			.authorities("AddressManager")
			.build();
		
		UserDetails transportManager = users
				.username("transportManager")
				.password(passwordEncoder.encode("pass"))
				.authorities("TransportManager")
				.build();
		
		UserDetails admin = users
			.username("admin")
			.password(passwordEncoder.encode("pass"))
			.authorities("admin", "AddressManager", "TransportManager") 
			.build();
			
		return new InMemoryUserDetailsManager(addressManager, transportManager, admin);
	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ 
		
		return http
				.csrf(csrf -> csrf.disable())   
				.authorizeHttpRequests(auth -> 
					auth
					.requestMatchers(HttpMethod.POST,"/api/login").permitAll()  
					.anyRequest().permitAll()
					
				)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)  
				.build();		
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
}
