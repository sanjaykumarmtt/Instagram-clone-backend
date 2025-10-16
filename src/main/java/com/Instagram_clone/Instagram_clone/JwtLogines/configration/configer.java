package com.Instagram_clone.Instagram_clone.JwtLogines.configration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.Instagram_clone.Instagram_clone.JwtLogines.Service.SecurityService;
 


@Configuration
@EnableWebSecurity
public class configer {
	
	@Autowired
	jwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain securityfilterchain(HttpSecurity http) throws Exception {
																
		http.authorizeHttpRequests(auth->auth
				.requestMatchers("/security/api/gethello").hasAnyAuthority("USER")
				.requestMatchers("/api/Post_data_file").hasAnyAuthority("USER")
				.requestMatchers("/security/api/getbay").hasAnyAuthority("ADMIN")
				.requestMatchers("/api-Security/getprofaildata").hasAnyAuthority("USER")
				.requestMatchers("/api/Tocken-Chane").hasAnyAuthority("USER")
				.requestMatchers("/api/Set_Story_data/{username}").hasAnyAuthority("USER")
				.requestMatchers("/api/Get_All_Story_data").permitAll()
				.requestMatchers("/api/Get_one_Story_data/{id}").permitAll()
				
				.requestMatchers("/api-Security/**").permitAll()
				.requestMatchers("/api/Get_All_post_data").permitAll()
				.requestMatchers(
					    "/v2/api-docs",
					    "/v3/api-docs",
					    "/v3/api-docs/**",
					    "/swagger-resources/**",
					    "/swagger-ui/**",
					    "/swagger-ui.html",
					    "/webjars/**").permitAll()
				.anyRequest().authenticated())		
//				.formLogin(form->form.permitAll()) 	
		.csrf(CS->CS.disable()) 
		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		
		return http.build();     
	}
	
    @Bean
    public UserDetailsService userDetailsService() {
        return new SecurityService();
    }
    @Bean
    public DaoAuthenticationProvider authenticationprovider() {
    	DaoAuthenticationProvider authprovider=new DaoAuthenticationProvider();
    	authprovider.setUserDetailsService(userDetailsService());
    	authprovider.setPasswordEncoder(passwordEncoder());
    	return authprovider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // BCrypt for secure password storage
    }
    @Bean
    public AuthenticationManager authentication() {
		return new ProviderManager(List.of(authenticationprovider()));
    	
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173/")); 
        configuration.setAllowedOrigins(Arrays.asList("https://ins-spring-boot-applicat-26fce.web.app/"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); 
        
        configuration.setAllowCredentials(true); 
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setMaxAge(3600L); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        
        return source;
    }

}
