package com.weddingPlannerBackend.controllers;

import com.weddingPlannerBackend.dtos.UserDto;
import com.weddingPlannerBackend.events.OnRegistrationCompleteEvent;
import com.weddingPlannerBackend.mappers.UserMapper;
import com.weddingPlannerBackend.model.User;
import com.weddingPlannerBackend.model.VerificationToken;
import com.weddingPlannerBackend.services.AuthenticationService;
import com.weddingPlannerBackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Base64;
import java.util.Calendar;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@Autowired
	private AuthenticationService authenticationService;

	@Value("${redirectUrl}")
	private String redirectUrl;

//	@Bean
//	@Autowired
//	public MappedInterceptor getUserMappedInterceptor(
//			AuthenticationController.MyHandlerInterceptor myHandlerInterceptor) {
//		return new MappedInterceptor(new String[] { "/api/users/logout" }, myHandlerInterceptor);
//	}

	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> registerUserAccount(@RequestBody UserDto userDto, HttpServletRequest request) {

		User registered = userService.registerNewUserAccount(userDto);
		if(registered == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("{ \"message\": \" Invalid data or duplicate email\" }");
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
		return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(registered));
	}

	@GetMapping(value = "/login", produces = "application/json")
	public ResponseEntity<String> loginUserAccount(@RequestHeader(value = "Authorization") String userInfo) {
		String[] arr = getDecodedInfo(userInfo);
		String email = arr[0];
		System.out.println("login password " + arr[1]);
		String password = arr[1];
		String token = authenticationService.createToken(email, password, "user");
		System.out.println("password" + password);
		if (token.isEmpty())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("{ \"message\": \" Invalid email or password\" }");
		if (token.equalsIgnoreCase("not verified"))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"message\": \" Email not verified\" }");
		return ResponseEntity.status(HttpStatus.OK).body("{ \"token\": \"" + token + "\" }");
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logoutUserAccount(@RequestHeader(value = "Authorization") String encodedToken) {
		String token = getDecodedToken(encodedToken);
		authenticationService.delToken(token);
		return ResponseEntity.status(HttpStatus.OK).body("{}");
	}

	@GetMapping("/registrationConfirm")
	public ResponseEntity<Void> confirmRegistration(WebRequest request, Model model,
			@RequestParam("token") String token) {

		VerificationToken verificationToken = userService.getVerificationToken(token);

		Calendar cal = Calendar.getInstance();
		if (verificationToken == null || (verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
		}

		User user = verificationToken.getUser();
		user.setVerified(true);
		userService.saveRegisteredUser(user);
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();

	}

	private String[] getDecodedInfo(String encodedUserInfo) {
		String encodedUsernamePassword = encodedUserInfo.substring("Basic ".length()).trim();
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] bytes = decoder.decode(encodedUsernamePassword);
		String decodedLine = new String(bytes);
		int separator = decodedLine.indexOf(':');
		String[] decodedUserInfo = new String[2];
		decodedUserInfo[0] = decodedLine.substring(0, separator);
		decodedUserInfo[1] = decodedLine.substring(separator + 1);
		return decodedUserInfo;
	}

	private String getDecodedToken(String encodedToken) {
		return encodedToken.split(" ")[1];
	}

}
