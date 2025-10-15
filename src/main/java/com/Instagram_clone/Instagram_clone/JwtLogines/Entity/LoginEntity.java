package com.Instagram_clone.Instagram_clone.JwtLogines.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class LoginEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Email
	@NotNull
	@NotBlank
	private String username;
	@NotNull
	@NotBlank
	private String fuallname;
	@NotNull
	@NotBlank
	private String Password;
	@NotNull
	@NotBlank
	private String role;
	@NotNull
	@NotBlank
	private String peofaile;

	public LoginEntity() {
		super();
	}



	public LoginEntity(int id, @Email @NotNull @NotBlank String username, @NotNull @NotBlank String fuallname,
			@NotNull @NotBlank String password, @NotNull @NotBlank String role, @NotNull @NotBlank String peofaile) {
		super();
		this.id = id;
		this.username = username;
		this.fuallname = fuallname;
		this.Password = password;
		this.role = role;
		this.peofaile = peofaile;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getFuallname() {
		return fuallname;
	}

	public void setFuallname(String fuallname) {
		this.fuallname = fuallname;
	}
}
