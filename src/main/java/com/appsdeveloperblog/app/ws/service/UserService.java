package com.appsdeveloperblog.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	public UserDto createUser(UserDto user);
	public UserDto getUser(String email);
	public UserDto getUserByUserId(String id);
	public UserDto updateUser(String userId, UserDto user);
	public void deleteUser(String userId);
	public List<UserDto> getUsers(int page, int limit);
	boolean verifyEmailToken(String token);
	boolean requestPasswordReset(String email);
	boolean resetPassword(String token, String password);
}
