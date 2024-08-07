package com.suvasish.Crypto.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.suvasish.Crypto.Domain.*;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	private String fullName;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ROLE_USER getRole() {
		return role;
	}
	public void setRole(ROLE_USER role) {
		this.role = role;
	}
	
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	//THE ABOVE ANNOTATION IS USED TO NOT GET THE PASSWORD FROM USER SIDE
	private String password;
	
	private ROLE_USER role=ROLE_USER.ROLE_CUSTOMER;
	@Embedded
	private TwoFactorAuth twoFactorAuth=new TwoFactorAuth();
	public TwoFactorAuth getTwoFactorAuth() {
		return twoFactorAuth;
	}
	public void setTwoFactorAuth(TwoFactorAuth twoFactorAuth) {
		this.twoFactorAuth = twoFactorAuth;
	}
}
