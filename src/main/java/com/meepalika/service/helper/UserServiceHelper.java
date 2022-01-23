package com.meepalika.service.helper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.meepalika.dto.UserDto;
import com.meepalika.entity.User;


@Component
public class UserServiceHelper {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public String encryptPassword(String password) {
		String encryptedPassword = passwordEncoder.encode(password);
		return encryptedPassword;
	}

	public UserDto convertToUserDto(User user) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDto = modelMapper.map(user, UserDto.class);
		userDto.setUserRole(user.getUserRole());
		return userDto;
	}

	public User convertToUserEntity(UserDto userDto) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		User user = modelMapper.map(userDto,User.class);
		user.setUserRole(userDto.getUserRole());
		return user;
	}
}
