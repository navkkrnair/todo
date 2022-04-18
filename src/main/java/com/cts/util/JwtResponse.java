package com.cts.util;

import lombok.Builder;
import lombok.Getter;

@Builder
public class JwtResponse {
	@Getter
	private String jwt;
}
