package com.afourathon.project_management_rest_api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
	
	@Value("${spring.security.user.name}")
	private String username;
	
	@Value("${spring.security.user.password}")
	private String password;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests()
	    	.antMatchers("/api/v1/**")
	    	.authenticated()
	        .and()
	        .authorizeHttpRequests()
	        .anyRequest()
            .permitAll()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .httpBasic()
        .and()
            .csrf()
            .disable();
	    return http.build();
	}
	
	@Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername(username)
            .password(passwordEncoder().encode(password))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(admin);
    }

	@Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }

}
