package com.cts.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cts.error.TodoError;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (RuntimeException e) {
			ObjectMapper mapper = new ObjectMapper();
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter()
					.write(mapper.writeValueAsString(TodoError.builder()
							.message(e.getMessage())
							.build()));
		}

	}

}
