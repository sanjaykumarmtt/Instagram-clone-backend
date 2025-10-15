package com.Instagram_clone.Instagram_clone.JwtLogines.Entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DTOLogindata {
	
	private String username;
	private String fuallname;
	private String Password;
	private String role;
	private String peofaile;
	private String otp;
	
	
	public DTOLogindata() {
		super();
	}
	public DTOLogindata(String username, String fuallname, String password, String role, String peofaile,String otp) {
		super();
		this.username = username;
		this.fuallname = fuallname;
		this.Password = password;
		this.role = role;
		this.peofaile = peofaile;
		this.otp=otp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFuallname() {
		return fuallname;
	}
	public void setFuallname(String fuallname) {
		this.fuallname = fuallname;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPeofaile() {
		return peofaile;
	}
	public void setPeofaile(String peofaile) {
		this.peofaile = peofaile;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
}
