package com.epam.weatherapp.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DefaultExceptionHandler extends Exception{

	private static final long serialVersionUID = 1L;

	public DefaultExceptionHandler(Exception e) {
		super("Something went wrong !!", e, true, false);
	}
}
