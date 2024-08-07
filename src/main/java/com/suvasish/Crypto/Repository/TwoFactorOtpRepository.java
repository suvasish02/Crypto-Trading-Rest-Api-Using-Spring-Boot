package com.suvasish.Crypto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suvasish.Crypto.Model.TwoFactorOtp;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOtp, String>{
	TwoFactorOtp findByUserId(Long userId);
}
