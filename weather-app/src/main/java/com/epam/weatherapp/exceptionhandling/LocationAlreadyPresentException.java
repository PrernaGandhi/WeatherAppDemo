package com.epam.weatherapp.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class LocationAlreadyPresentException extends Exception {

	private static final long serialVersionUID = 1L;

	public LocationAlreadyPresentException(String city,Exception e) {
		super("Location Already Present in database: ".concat(city),e, true, false);

	}

	
	
}
