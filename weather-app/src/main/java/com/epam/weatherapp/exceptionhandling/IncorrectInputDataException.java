package com.epam.weatherapp.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.epam.weatherapp.dto.WeatherDetailsDTO;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IncorrectInputDataException extends Exception {

	private static final long serialVersionUID = 1L;

	public IncorrectInputDataException(WeatherDetailsDTO weatherDetailsDTO, Exception e) {
		super("Incorrect Input Data : Location Name : " + weatherDetailsDTO.getLocationName() + "  Temperature : "
				+ weatherDetailsDTO.getTemperature(), e, true, false);

	}
}
