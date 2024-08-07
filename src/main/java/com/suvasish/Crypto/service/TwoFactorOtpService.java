package com.suvasish.Crypto.service;

import com.suvasish.Crypto.Model.TwoFactorOtp;
import com.suvasish.Crypto.Model.User;

public interface TwoFactorOtpService {
	TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt);

    TwoFactorOtp findByUser(Long userId);

    TwoFactorOtp findById(String id);

    boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp,String otp);

    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOTP);
}
