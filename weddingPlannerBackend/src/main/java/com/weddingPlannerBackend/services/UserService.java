package com.weddingPlannerBackend.services;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weddingPlannerBackend.dtos.UserDto;
import com.weddingPlannerBackend.mappers.UserMapper;
import com.weddingPlannerBackend.model.User;
import com.weddingPlannerBackend.model.VerificationToken;
import com.weddingPlannerBackend.repositories.UserRepo;
import com.weddingPlannerBackend.repositories.VerificationTokenRepository;


@Service
@Transactional
public class UserService implements IUserService {
	@Autowired
	private UserRepo repository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private VerificationTokenRepository tokenRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	public List<User> getAllUsers() {
		// TODO replace user with userDto

		return repository.findAll();
	}

	@Override
	public User registerNewUserAccount(UserDto userDto) {

		User user = userMapper.fromDto(userDto);
		if (!validateUser(user) || emailExist(user.getEmail()))
			return null;

		return repository.save(user);
	}

	
	private boolean emailExist(String email) {
		return repository.findById(email).isPresent();
	}

	private boolean validateUser(User user) {
		return validateEmail(user.getEmail()) && user.getName() != null && user.getPassword() != null;
	}

	private boolean validateEmail(String email) {
		Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = emailPattern.matcher(email);

		return matcher.matches();
	}

	@Override
	public User getUser(String verificationToken) {
		return tokenRepository.findByToken(verificationToken).getUser();
	}

	@Override
	public VerificationToken getVerificationToken(String VerificationToken) {
		return tokenRepository.findByToken(VerificationToken);
	}

	@Override
	public void saveRegisteredUser(User user) {
		repository.save(user);
	}

	@Override
	public void createVerificationToken(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}
}
