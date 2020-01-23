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

import com.epam.weatherapp.dto.WeatherDetailsDTO;
import com.epam.weatherapp.exceptionhandling.DefaultExceptionHandler;
import com.epam.weatherapp.exceptionhandling.IncorrectInputDataException;
import com.epam.weatherapp.exceptionhandling.LocationAlreadyPresentException;
import com.epam.weatherapp.mapper.WeatherDetailsMapper;
import com.epam.weatherapp.service.WeatherService;

@RestController
public class WeatherController {
	@Autowired
	WeatherService weatherService;
	@Autowired
	WeatherDetailsMapper weatherDetailsMapper;

	@GetMapping("/weather-details/{city}")
	public ResponseEntity<WeatherDetailsDTO> getWeatherDetailsForSelectedCity(@PathVariable String city) throws DefaultExceptionHandler {
		ResponseEntity<WeatherDetailsDTO> response = null;
		try {
			Optional<WeatherDetailsDTO> weatherDetailsDTO = weatherService.getWeatherDetailsByLocationName(city);
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
			if (weatherDetailsDTO.isPresent()) {
				response = ResponseEntity.status(HttpStatus.FOUND).body(weatherDetailsDTO.get());
			}
		} catch (Exception e) {
			throw new DefaultExceptionHandler(e);
		}
		return response;
	}
	@GetMapping("/weather-details/all")
	public ResponseEntity<List<WeatherDetailsDTO>> getWeatherDetailsForAllCities() throws DefaultExceptionHandler {
		ResponseEntity<List<WeatherDetailsDTO>> response = null;
		try {
			List<WeatherDetailsDTO> weatherDetailsDTO = weatherService.getAllWeatherDetails();
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
			if (weatherDetailsDTO != null) {
				response = ResponseEntity.status(HttpStatus.FOUND).body(weatherDetailsDTO);
			}
		} catch (Exception e) {
			throw new DefaultExceptionHandler(e);
		}
		return response;
	}

	@PostMapping("/add-weather-details")
	public ResponseEntity<WeatherDetailsDTO> addWeatherDetailsForSelectedCity(@RequestBody WeatherDetailsDTO weatherDetailsDTO) throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		ResponseEntity<WeatherDetailsDTO> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		try {
			weatherDetailsDTO = weatherService.updateWeatherDetails(weatherDetailsDTO);			
			if (weatherDetailsDTO != null) {
				response = ResponseEntity.status(HttpStatus.CREATED).body(weatherDetailsDTO);
			}
		} catch (Exception e) {
			if(e.getClass().isAssignableFrom((DataIntegrityViolationException.class))) {
				throw new LocationAlreadyPresentException(weatherDetailsDTO.getLocationName(),e);
			}else if(e.getClass().isAssignableFrom(ConstraintViolationException.class)) {
				throw new IncorrectInputDataException(weatherDetailsDTO,e);	
			}else {
				throw new DefaultExceptionHandler(e);
			}
		}
		return response;
	}
}
