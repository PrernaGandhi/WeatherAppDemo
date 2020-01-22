package com.epam.weatherapp.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.weatherapp.entity.WeatherDetails;
import com.epam.weatherapp.exceptionhandling.DefaultExceptionHandler;
import com.epam.weatherapp.exceptionhandling.IncorrectInputDataException;
import com.epam.weatherapp.exceptionhandling.LocationAlreadyPresentException;
import com.epam.weatherapp.service.WeatherService;

@RestController
public class WeatherController {
	@Autowired
	WeatherService weatherService;

	@GetMapping("/weather-details/{city}")
	public ResponseEntity<WeatherDetails> getWeatherDetailsForSelectedCity(@PathVariable String city) throws DefaultExceptionHandler {
		ResponseEntity<WeatherDetails> response = null;
		try {
			Optional<WeatherDetails> weatherDetails = weatherService.getWeatherDetailsByLocationName(city);
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
			if (weatherDetails.isPresent()) {
				response = ResponseEntity.status(HttpStatus.FOUND).body(weatherDetails.get());
			}
		} catch (Exception e) {
			throw new DefaultExceptionHandler(e);
		}
		return response;
	}
	@GetMapping("/weather-details/all")
	public ResponseEntity<List<WeatherDetails>> getWeatherDetailsForAllCities() throws DefaultExceptionHandler {
		ResponseEntity<List<WeatherDetails>> response = null;
		try {
			List<WeatherDetails> weatherDetails = weatherService.getAllWeatherDetails();
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
			if (weatherDetails != null) {
				response = ResponseEntity.status(HttpStatus.FOUND).body(weatherDetails);
			}
		} catch (Exception e) {
			throw new DefaultExceptionHandler(e);
		}
		return response;
	}

	@PostMapping("/add-weather-details")
	public ResponseEntity<WeatherDetails> addWeatherDetailsForSelectedCity(@RequestBody WeatherDetails weatherDetails) throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		ResponseEntity<WeatherDetails> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		try {
			weatherDetails = weatherService.updateWeatherDetails(weatherDetails);			
			if (weatherDetails != null) {
				response = ResponseEntity.status(HttpStatus.FOUND).body(weatherDetails);
			}
		} catch (Exception e) {
			if(e.getClass().isAssignableFrom((DataIntegrityViolationException.class))) {
				throw new LocationAlreadyPresentException(weatherDetails.getLocationName(),e);
			}else if(e.getClass().isAssignableFrom(ConstraintViolationException.class)) {
				throw new IncorrectInputDataException(weatherDetails,e);	
			}else {
				throw new DefaultExceptionHandler(e);
			}
		}
		return response;
	}
}
