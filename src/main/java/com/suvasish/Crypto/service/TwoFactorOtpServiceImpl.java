package com.suvasish.Crypto.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suvasish.Crypto.Model.TwoFactorOtp;
import com.suvasish.Crypto.Model.User;
import com.suvasish.Crypto.Repository.TwoFactorOtpRepository;
@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService{
	@Autowired
	private TwoFactorOtpRepository twoFactorOtpRepository;
	@Override
	public TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt) {
		UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        TwoFactorOtp twoFactorOTP=new TwoFactorOtp();
        twoFactorOTP.setId(id);
        twoFactorOTP.setUser(user);
        twoFactorOTP.setOtp(otp);
        twoFactorOTP.setJwt(jwt);
        return twoFactorOtpRepository.save(twoFactorOTP);
		
	}

	@Override
	public TwoFactorOtp findByUser(Long userId) {
		// TODO Auto-generated method stub
		 return twoFactorOtpRepository.findByUserId(userId);
		
	}

	@Override
	public TwoFactorOtp findById(String id) {
		// TODO Auto-generated method stub
		Optional<TwoFactorOtp> twoFactorOtp=twoFactorOtpRepository.findById(id);
        return twoFactorOtp.orElse(null);
	}

	@Override
	public boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp) {
		// TODO Auto-generated method stub
		return twoFactorOtp.getOtp().equals(otp);
	}

	@Override
	public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOTP) {
		// TODO Auto-generated method stub
		twoFactorOtpRepository.delete(twoFactorOTP);
		
	}

}
