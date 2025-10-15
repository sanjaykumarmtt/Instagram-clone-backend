package com.Instagram_clone.Instagram_clone.JwtLogines.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Instagram_clone.Instagram_clone.ExceptionHan.HostelException;
import com.Instagram_clone.Instagram_clone.JwtLogines.Entity.DTOLogindata;
import com.Instagram_clone.Instagram_clone.JwtLogines.Entity.LoginEntity;
import com.Instagram_clone.Instagram_clone.JwtLogines.Service.JwtGenaratTocken;
import com.Instagram_clone.Instagram_clone.JwtLogines.Service.SecurityService;

@RestController
@CrossOrigin
@RequestMapping("/api-Security")
public class SecurityController {
	
	@Autowired
	JwtGenaratTocken tocken;
	
	@Autowired
	SecurityService Service;
	
	@Autowired
	AuthenticationManager authmanager;
	
	@Autowired
	PasswordEncoder PasswordEncoder;
	
	@GetMapping("/Get-All-user-data")
	public ResponseEntity<List<LoginEntity>> getAlluserdata(){
		return ResponseEntity.ok(Service.getAlluserdata());
	}

	
	@PostMapping("/Register")
	public ResponseEntity<LoginEntity> Register(@RequestPart("regist") DTOLogindata se,@RequestPart("profilePic") MultipartFile prof) throws HostelException, IOException {

		if(se!=null && prof!=null) {
			se.setPassword(PasswordEncoder.encode(se.getPassword()));
			return ResponseEntity.ok(Service.Register(se,prof));
		}
		throw new HostelException("You have left some information blank, please enter that too");
		
		
	}
	@GetMapping("/getprofaildata")
	public Optional<LoginEntity> Getuserdata(@RequestParam String Username) {
		return Service.Getuserdata(Username);	
	}
	
	@PostMapping("/Register-sende-OTP")
	public ResponseEntity<String> Register(@RequestParam String email){
		Service.RegistersendOTP(email);
		return ResponseEntity.ok("OTP sended successful please go to check your mail");
	}
	@PostMapping("/changeposs-sende-OTP")
	public ResponseEntity<String> changeposssendotp(@RequestParam String email){
		Service.ChangePosssendOTP(email);
		return ResponseEntity.ok("OTP sended successful please go to check your mail");
	}
	@PostMapping("/OTP-Verfecation")
	public boolean OTP_Verfecation(@RequestParam String otp){
		if(Service.verifyOTP(otp)) {
			return true;
		}
		throw new HostelException("You Otp is Invalide Please type valid OTP");
	}
	@PostMapping("/Forget-password")
	public ResponseEntity<String> Forgetpassword(@RequestBody DTOLogindata dto){
		
		dto.setPassword(PasswordEncoder.encode(dto.getPassword()));
		Service.Forgetpassword(dto);
		return ResponseEntity.ok("Password changed successful go to login");
	}
	@PostMapping("/Logine")
	public ResponseEntity<Map<String,String>> login(@RequestBody DTOLogindata se) {
		try {
//			System.out.println(se.getUsername()+" "+se.getPassword());
			Authentication authe=authmanager.
					authenticate(new UsernamePasswordAuthenticationToken(se.getUsername(),se.getPassword()));
		
			UserDetails userDeta=(UserDetails) authe.getPrincipal();
			
			String tocke=tocken.generatToken(userDeta);
			return ResponseEntity.ok(Map.of("token",tocke));	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error","Invalied UserName Or Posswored"));	
		}
	}

}
