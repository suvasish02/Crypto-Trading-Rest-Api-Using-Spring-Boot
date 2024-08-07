package com.suvasish.Crypto.Model;

import com.suvasish.Crypto.Domain.VerificationType;

import jakarta.persistence.Entity;
import lombok.Data;


@Data
public class TwoFactorAuth {
	private boolean isEnabled=false;
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public VerificationType getSendTo() {
		return sendTo;
	}
	public void setSendTo(VerificationType sendTo) {
		this.sendTo = sendTo;
	}
	private VerificationType sendTo;
}
