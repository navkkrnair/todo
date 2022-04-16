package com.cts.service.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.service.AuthenticationService;
import com.cts.service.TodoUserAuthenticationProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoUserAuthenticationProviderImpl implements TodoUserAuthenticationProvider {

	private final AuthenticationService authenticationService;
	private final BCryptPasswordEncoder encoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("Authenticating user {}", authentication.getName());
		String username = authentication.getName();
		UserDetails userDetails = authenticationService.loadUserByUsername(authentication.getName());

		log.info("Matching the credentials");
		if (encoder.matches((String) authentication.getCredentials(), userDetails.getPassword())) {
			return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),
					userDetails.getAuthorities());
		} else {
			log.error("Passwords doesn't match");
			throw new BadCredentialsException("User doesn't exist");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
