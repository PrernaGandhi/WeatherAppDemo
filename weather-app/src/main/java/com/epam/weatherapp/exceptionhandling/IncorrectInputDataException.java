package com.epam.weatherapp.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.epam.weatherapp.entity.WeatherDetails;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IncorrectInputDataException extends Exception {

	private static final long serialVersionUID = 1L;

	public IncorrectInputDataException(WeatherDetails weatherDetails, Exception e) {
		super("Incorrect Input Data : Location Name : " + weatherDetails.getLocationName() + "  Temperature : "
				+ weatherDetails.getTemperature(), e, true, false);

	}
}
