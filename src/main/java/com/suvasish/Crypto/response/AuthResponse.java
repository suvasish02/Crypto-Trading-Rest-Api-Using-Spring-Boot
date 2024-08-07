package com.suvasish.Crypto.response;

import lombok.Data;


public class AuthResponse {
	private String jwt;
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private boolean status;
	private String message;
	private boolean isTwoFactorAuthEnabled;
	public boolean isTwoFactorAuthEnabled() {
		return isTwoFactorAuthEnabled;
	}
	public void setTwoFactorAuthEnabled(boolean isTwoFactorAuthEnabled) {
		this.isTwoFactorAuthEnabled = isTwoFactorAuthEnabled;
	}
	private String session;
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
}
