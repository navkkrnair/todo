package com.cts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cts.domain.TodoUser;
import com.cts.error.TodoError;
import com.cts.service.TodoUserAuthenticationProvider;
import com.cts.util.JwtResponse;
import com.cts.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

	private final TodoUserAuthenticationProvider authenticationProvider;
	private final JwtUtil jwtUtil;

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody TodoUser todoUser) {
		log.info("User {} loggin in", todoUser);
		Authentication toAuthenticate = new UsernamePasswordAuthenticationToken(todoUser.getUsername(),
				todoUser.getPassword());
		try {
			Authentication authenticatedUser = authenticationProvider.authenticate(toAuthenticate);
			String token = jwtUtil.generateToken(authenticatedUser);
			return ResponseEntity.ok()
					.body(JwtResponse.builder()
							.jwt(token)
							.build());
		} catch (BadCredentialsException e) {
			log.info("Exception {}", e.getMessage());
			return ResponseEntity.badRequest()
					.body(TodoError.builder()
							.message(e.getMessage())
							.build());
		}
	}

}
