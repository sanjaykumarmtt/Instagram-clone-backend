package com.Instagram_clone.Instagram_clone.JwtLogines.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.Instagram_clone.Instagram_clone.ExceptionHan.HostelException;
import com.Instagram_clone.Instagram_clone.JwtLogines.Entity.DTOLogindata;
import com.Instagram_clone.Instagram_clone.JwtLogines.Entity.LoginEntity;
import com.Instagram_clone.Instagram_clone.JwtLogines.Entity.OTP;
import com.Instagram_clone.Instagram_clone.JwtLogines.Repositroy.Otprepository;
import com.Instagram_clone.Instagram_clone.JwtLogines.Repositroy.SecurityRepositry;
import com.Instagram_clone.Instagram_clone.servise.AwsServise;

import jakarta.mail.internet.MimeMessage;

@Service
public class SecurityService implements UserDetailsService {

	@Autowired
	SecurityRepositry SecurityRepositry;
	@Autowired
	Otprepository Otprepository;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	AwsServise AwsServise;
	
	@Autowired
	private SpringTemplateEngine templateEngine;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginEntity se = SecurityRepositry.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User name or passwored Increate"));

		System.out.println(se.getUsername());
		System.out.println(se.getPassword());
		return new User(se.getUsername(), se.getPassword(),
				Collections.singleton(new SimpleGrantedAuthority(se.getRole())));
	}

	public LoginEntity Register(DTOLogindata dto, MultipartFile prof) throws HostelException, IOException {
		if (verifyOTP(dto.getOtp())) {
			String failurl = AwsServise.AwsS3fail_stor(prof);
			if (failurl != null) {
				LoginEntity LoginEntity = new LoginEntity();
				LoginEntity.setFuallname(dto.getFuallname());
				LoginEntity.setUsername(dto.getUsername());
				LoginEntity.setPassword(dto.getPassword());
				LoginEntity.setRole(dto.getRole());
				LoginEntity.setPeofaile(failurl);
				return SecurityRepositry.save(LoginEntity);
			}
			throw new HostelException("NetWork Error Please try again");
		}
		throw new HostelException("OTP InValid Please type valid OTP");

	}
	public List<LoginEntity> getAlluserdata(){
		List<LoginEntity> logi=SecurityRepositry.findAll();
		List<LoginEntity> loginlist=new ArrayList<>();
		for(LoginEntity l:logi) {
			l.setPeofaile(AwsServise.oneUrlForImage(l.getPeofaile()));
			loginlist.add(l);
		}
		return loginlist;
	}

	public boolean verifyOTP(String otp) {
		return Otprepository.findByOtp(otp)
				.filter(o -> o.getOtp().equals(otp) && o.getExpiryTime().isAfter(LocalDateTime.now())).isPresent();
	}

	// gen otp meth
	public String generateOTP() {
		return String.format("%06d", new Random().nextInt(999999));
	}

	// create otp
	public void RegistersendOTP(String email) throws HostelException {
		LoginEntity loginEntity = SecurityRepositry.getByusername(email);
		if (loginEntity == null) {
			String otp = generateOTP();
			OTP otpEntity = new OTP();
			otpEntity.setEmail(email);
			otpEntity.setOtp(otp);
			otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
			System.out.println(email + " " + otp);
			Otprepository.save(otpEntity);
			sendEmail(email, otp);

		} else {
			throw new HostelException("You already Register Please go To Login");
		}
	}
	public void Forgetpassword(DTOLogindata dto) throws HostelException{
//		try {
		String user=dto.getUsername();
		String posswo=dto.getPassword();
		
		System.out.println(dto.getUsername());
		System.out.println(dto.getPassword());
		
		LoginEntity se = SecurityRepositry.getByusername(user);
		
		
		se.setPassword(posswo);
		
		System.out.println(se.getPassword()+" "+se.getUsername()+" "+se.getFuallname());
//		SecurityRepositry.save(se);
//		}catch(Exception e) {
//			throw new HostelException("Forgetpassword Failde please try again "+e.getMessage());
//			
//		}
	}
	public Optional<LoginEntity> Getuserdata(String Username) {
		Optional<LoginEntity> ent=SecurityRepositry.findByUsername(Username);
		ent.get().setPeofaile(AwsServise.oneUrlForImage(ent.get().getPeofaile()));
		return ent;
	}

	// create otp
	public void ChangePosssendOTP(String email) throws HostelException {
		try {
			String otp = generateOTP();
			OTP otpEntity = new OTP();
			otpEntity.setEmail(email);
			otpEntity.setOtp(otp);
			otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
			System.out.println(email + " " + otp);
			Otprepository.save(otpEntity);
			sendEmail(email, otp);

		} catch (Exception e) {
			throw new HostelException(e.getMessage());
		}
	}

	

	public void sendEmail(String toEmail, String generatedOtp) {

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			String senderEmail = "instagramclone60@gmail.com";
			String senderName = "Instagram Clone";
			helper.setFrom(senderEmail, senderName);
			helper.setTo(toEmail);
			helper.setSubject("Your One-Time Password (OTP) for Instagram Clone");

			Context context = new Context();
			context.setVariable("otp", generatedOtp);
			context.setVariable("userName", toEmail.substring(0, toEmail.indexOf('@')));
			String htmlContent = templateEngine.process("otp-template", context);

			helper.setText(htmlContent, true);
			ClassPathResource image = new ClassPathResource(
					"https://upload.wikimedia.org/wikipedia/commons/a/a5/Instagram_icon.png");
			if (image.exists()) {
				helper.addInline("logo", image);
			}
			mailSender.send(message);

		} catch (Exception e) {
			throw new RuntimeException("Error sending email with HTML template: " + e.getMessage());
		}

    }

}
