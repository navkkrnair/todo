package com.cts.error;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoError {
	@Getter
	private String message;

	@JsonInclude(value = Include.NON_EMPTY)
	@Getter
	private List<String> errors;

}
