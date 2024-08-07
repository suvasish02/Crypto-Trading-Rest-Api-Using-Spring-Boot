package com.suvasish.Crypto.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.suvasish.Crypto.Config.JwtProvider;
import com.suvasish.Crypto.Exception.UserException;
import com.suvasish.Crypto.Model.TwoFactorOtp;
import com.suvasish.Crypto.Model.User;
import com.suvasish.Crypto.Repository.UserRepository;
import com.suvasish.Crypto.Utils.OtpUtils;
import com.suvasish.Crypto.request.LoginRequest;
import com.suvasish.Crypto.response.AuthResponse;
import com.suvasish.Crypto.service.CustomUserDetailsService;
import com.suvasish.Crypto.service.EmailService;
import com.suvasish.Crypto.service.TwoFactorOtpService;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private TwoFactorOtpService twoFactorOtpService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private EmailService emailService;
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> register(@RequestBody User user)throws UserException{
		User isEmailExist = userRepository.findByEmail(user.getEmail());
		if (isEmailExist!=null) {
			throw new UserException("Email Is Already Used With Another Account");
		}
		User newUser=new User();
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		newUser.setFullName(user.getFullName());
		User savedUser= userRepository.save(newUser);
		Authentication authentication=new 
				UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt=JwtProvider.generateToken(authentication);
		//System.out.println(jwt);
		AuthResponse res=new AuthResponse();
		res.setJwt(jwt);
		res.setStatus(true);
		res.setMessage("Register Success");
		return new ResponseEntity<AuthResponse>(res,HttpStatus.CREATED);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest loginRequest) throws UserException, MessagingException {
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		System.out.println(username + " ----- " + password);

		Authentication authentication = authenticate(username, password);

		User user=userRepository.findByEmail(username);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = JwtProvider.generateToken(authentication);

		if(user.getTwoFactorAuth().isEnabled()){
			AuthResponse authResponse = new AuthResponse();
			authResponse.setMessage("Two factor authentication enabled");
			authResponse.setTwoFactorAuthEnabled(true);

			String otp= OtpUtils.generateOTP();

			TwoFactorOtp oldTwoFactorOTP=twoFactorOtpService.findByUser(user.getId());
			if(oldTwoFactorOTP!=null){
				twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
			}


			TwoFactorOtp twoFactorOTP=twoFactorOtpService.createTwoFactorOtp(user,otp,token);

			emailService.sendVerificationOtpEmail(user.getEmail(),otp);

			authResponse.setSession(twoFactorOTP.getId());
			return new ResponseEntity<>(authResponse, HttpStatus.OK);
		}

		AuthResponse authResponse = new AuthResponse();

		authResponse.setMessage("Login Success");
		authResponse.setJwt(token);

		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

		System.out.println("sign in userDetails - " + userDetails);

		if (userDetails == null) {
			System.out.println("sign in userDetails - null " + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			System.out.println("sign in userDetails - password not match " + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
	@PostMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponse> verifySigningOtp(
			@PathVariable String otp,
			@RequestParam String id
	) throws Exception {


		TwoFactorOtp twoFactorOTP = twoFactorOtpService.findById(id);

		if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP,otp)){
			AuthResponse authResponse = new AuthResponse();
			authResponse.setMessage("Two factor authentication verified");
			authResponse.setTwoFactorAuthEnabled(true);
			authResponse.setJwt(twoFactorOTP.getJwt());
			return new ResponseEntity<>(authResponse, HttpStatus.OK);
		}
		throw new Exception("invalid otp");
	}
}
