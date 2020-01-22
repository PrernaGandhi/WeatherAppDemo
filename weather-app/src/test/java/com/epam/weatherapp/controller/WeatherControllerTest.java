package com.epam.weatherapp.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.epam.weatherapp.entity.WeatherDetails;
import com.epam.weatherapp.exceptionhandling.DefaultExceptionHandler;
import com.epam.weatherapp.exceptionhandling.IncorrectInputDataException;
import com.epam.weatherapp.exceptionhandling.LocationAlreadyPresentException;
import com.epam.weatherapp.service.WeatherService;

class WeatherControllerTest {

	@Mock
	WeatherService weatherService;
	Optional<WeatherDetails> weatherDetailsOptional;
	@Mock
	List<WeatherDetails> weatherDetailsList;
	
	WeatherDetails weatherDetails =new WeatherDetails();

	@InjectMocks
	WeatherController weatherController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetWeatherDetailsForSelectedCityPresentInDataBase() throws DefaultExceptionHandler {
		weatherDetailsOptional = Optional.of(new WeatherDetails());
		doReturn(weatherDetailsOptional).when(weatherService).getWeatherDetailsByLocationName(Mockito.anyString());
		ResponseEntity<WeatherDetails> response = weatherController.getWeatherDetailsForSelectedCity("hyderabad");
		assertTrue(response.getStatusCode().equals(HttpStatus.FOUND));
		verify(weatherService).getWeatherDetailsByLocationName("hyderabad");
	}

	@Test
	void testGetWeatherDetailsForSelectedCityNotPresentInDatabase() throws DefaultExceptionHandler {
		weatherDetailsOptional = Optional.ofNullable(null);
		doReturn(weatherDetailsOptional).when(weatherService).getWeatherDetailsByLocationName(Mockito.anyString());
		ResponseEntity<WeatherDetails> response = weatherController.getWeatherDetailsForSelectedCity("hyderabad");
		assertTrue(response.getStatusCode().equals(HttpStatus.NO_CONTENT));
		verify(weatherService).getWeatherDetailsByLocationName("hyderabad");
	}

	@Test
	void testGetWeatherDetailsForSelectedCityWithException() throws DefaultExceptionHandler {
		doThrow(NullPointerException.class).when(weatherService).getWeatherDetailsByLocationName(Mockito.anyString());
		Exception e = assertThrows(DefaultExceptionHandler.class, () -> {
			weatherController.getWeatherDetailsForSelectedCity("hyderabad");
		});
		assertTrue(e.getMessage().contains("Something went wrong"));
		verify(weatherService).getWeatherDetailsByLocationName("hyderabad");
	}

	@Test
	void testGetWeatherDetailsForAllCitiesPositive() throws DefaultExceptionHandler {
		doReturn(weatherDetailsList).when(weatherService).getAllWeatherDetails();
		ResponseEntity<List<WeatherDetails>> response = weatherController.getWeatherDetailsForAllCities();
		assertTrue(response.getStatusCode().equals(HttpStatus.FOUND));
		verify(weatherService).getAllWeatherDetails();
	}

	@Test
	void testGetWeatherDetailsForAllCitiesNegative() throws DefaultExceptionHandler {
		doReturn(null).when(weatherService).getAllWeatherDetails();
		ResponseEntity<List<WeatherDetails>> response = weatherController.getWeatherDetailsForAllCities();
		assertTrue(response.getStatusCode().equals(HttpStatus.NO_CONTENT));
		verify(weatherService).getAllWeatherDetails();
	}

	@Test
	void testGetWeatherDetailsForAllCitiesException() throws DefaultExceptionHandler {
		doThrow(NullPointerException.class).when(weatherService).getAllWeatherDetails();
		Exception e = assertThrows(DefaultExceptionHandler.class, () -> {
			weatherController.getWeatherDetailsForAllCities();
		});
		assertTrue(e.getMessage().contains("Something went wrong"));
		verify(weatherService).getAllWeatherDetails();
	}

	@Test
	void testAddWeatherDetailsForSelectedCityPositive()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		doReturn(weatherDetails).when(weatherService).updateWeatherDetails(weatherDetails);
		ResponseEntity<WeatherDetails> response = weatherController.addWeatherDetailsForSelectedCity(weatherDetails);
		assertTrue(response.getStatusCode().equals(HttpStatus.FOUND));
		verify(weatherService).updateWeatherDetails(weatherDetails);
	}

	@Test
	void testAddWeatherDetailsForSelectedCityNegative()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		doReturn(null).when(weatherService).updateWeatherDetails(weatherDetails);
		ResponseEntity<WeatherDetails> response = weatherController.addWeatherDetailsForSelectedCity(weatherDetails);
		assertTrue(response.getStatusCode().equals(HttpStatus.NO_CONTENT));
		verify(weatherService).updateWeatherDetails(weatherDetails);
	}

	@Test
	void testAddWeatherDetailsForSelectedCityLocationAlreadyPresentException()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		weatherDetails.setLocationName("hyderabad");
		weatherDetails.setTemperature("24 degrees");
		doThrow(DataIntegrityViolationException.class).when(weatherService).updateWeatherDetails(weatherDetails);
		Exception e = assertThrows(LocationAlreadyPresentException.class, () -> {
			weatherController.addWeatherDetailsForSelectedCity(weatherDetails);
		});
		System.out.println(e.getMessage());
		assertTrue(e.getMessage().contains("Location Already Present"));
		verify(weatherService).updateWeatherDetails(weatherDetails);
	}
	@Test
	void testAddWeatherDetailsForSelectedCityIncorrectInputDataException()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		weatherDetails.setLocationName("hyderabad");
		weatherDetails.setTemperature("24 degrees");
		doThrow(ConstraintViolationException.class).when(weatherService).updateWeatherDetails(weatherDetails);
		Exception e = assertThrows(IncorrectInputDataException.class, () -> {
			weatherController.addWeatherDetailsForSelectedCity(weatherDetails);
		});
		System.out.println(e.getMessage());
		assertTrue(e.getMessage().contains("Incorrect Input Data"));
		verify(weatherService).updateWeatherDetails(weatherDetails);
	}
	@Test
	void testAddWeatherDetailsForSelectedCityDefaultExceptionHandler()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		weatherDetails.setLocationName("hyderabad");
		weatherDetails.setTemperature("24 degrees");
		doThrow(NullPointerException.class).when(weatherService).updateWeatherDetails(weatherDetails);
		Exception e = assertThrows(DefaultExceptionHandler.class, () -> {
			weatherController.addWeatherDetailsForSelectedCity(weatherDetails);
		});
		System.out.println(e.getMessage());
		assertTrue(e.getMessage().contains("Something went wrong"));
		verify(weatherService).updateWeatherDetails(weatherDetails);
	}
}
