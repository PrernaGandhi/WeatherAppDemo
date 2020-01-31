package com.epam.weatherapp.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.weatherapp.dto.SearchCriteria;
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

	@PostMapping("/weather-details/search")
	public ResponseEntity<List<WeatherDetailsDTO>> getWeatherDetails(
			@RequestBody(required = false) SearchCriteria searchCriteria) throws DefaultExceptionHandler {
		ResponseEntity<List<WeatherDetailsDTO>> response = null;
		try {
			List<WeatherDetailsDTO> weatherDetailsDTOList = weatherService.getAllWeatherDetails(searchCriteria);
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			if (weatherDetailsDTOList != null) {
				response = ResponseEntity.status(HttpStatus.OK).body(weatherDetailsDTOList);
			}
		} catch (Exception e) {
			throw new DefaultExceptionHandler(e);
		}
		return response;
	}

	@DeleteMapping(name = "/weather-details/{city}")
	public ResponseEntity<String> deleteWeatherDetails(@PathVariable String city) throws DefaultExceptionHandler {
		ResponseEntity<String> response = null;
		try {
			Optional<WeatherDetailsDTO> weatherDetailsDTO = weatherService.getWeatherDetailsByLocationName(city);
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not available in database to delete!!");
			if (weatherDetailsDTO.isPresent()) {
				boolean isSuccess = weatherService.deleteByLocationName(city);
				response = ResponseEntity.status(HttpStatus.CONFLICT).body("Unable to delete Record!!");
				if (isSuccess) {
					response = ResponseEntity.status(HttpStatus.OK).body("Record Deleted Successfully!!");
				}
			}
		} catch (Exception e) {
			throw new DefaultExceptionHandler(e);
		}
		return response;
	}

	@PostMapping("/weather-details")
	public ResponseEntity<WeatherDetailsDTO> addWeatherDetailsForSelectedCity(
			@RequestBody WeatherDetailsDTO weatherDetailsDTO)
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		ResponseEntity<WeatherDetailsDTO> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		try {
			weatherDetailsDTO = weatherService.addWeatherDetails(weatherDetailsDTO);
			if (weatherDetailsDTO != null) {
				response = ResponseEntity.status(HttpStatus.CREATED).body(weatherDetailsDTO);
			}
		} catch (DataIntegrityViolationException e) {
			throw new LocationAlreadyPresentException(weatherDetailsDTO.getLocationName(), e);
		} catch (ConstraintViolationException e) {
			throw new IncorrectInputDataException(weatherDetailsDTO, e);
		} catch (Exception e) {
			throw new DefaultExceptionHandler(e);
		}
		return response;
	}
}
