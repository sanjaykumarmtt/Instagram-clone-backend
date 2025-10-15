package com.Instagram_clone.Instagram_clone.JwtLogines.configration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Instagram_clone.Instagram_clone.JwtLogines.Service.JwtGenaratTocken;
import com.Instagram_clone.Instagram_clone.JwtLogines.Service.SecurityService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class jwtFilter extends OncePerRequestFilter{

	@Autowired
	JwtGenaratTocken jWTSservice;
	@Autowired
	SecurityService context;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 String authHeader=request.getHeader("Authorization");
	     String tocken=null;
	     String username=null;
	   
	     if(authHeader !=null && authHeader.startsWith("Bearer ")) {
	    	 tocken=authHeader.substring(7);
	    	 System.out.println(tocken);
	    	 username=jWTSservice.extractUserName(tocken);
	    	  System.out.println(authHeader +" sanjay tock");
	     }
	     
	     if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
	    	 UserDetails userdetails=context.loadUserByUsername(username);
	    	 try {
	    		  if(jWTSservice.validateToken(tocken,userdetails)) {
	    		 
	    			UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
	    					userdetails,null,userdetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
	    		 
	    	 }
	    	 }catch(Exception e) {
	    		 System.out.println(e);
	    		 System.out.println("JWT Validation Failed: " + e.getMessage());
	    	 }
	    	
	     }
	 	filterChain.doFilter(request, response);	
	}
}