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
import org.mockito.Spy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.epam.weatherapp.dto.WeatherDetailsDTO;
import com.epam.weatherapp.entity.WeatherDetails;
import com.epam.weatherapp.exceptionhandling.DefaultExceptionHandler;
import com.epam.weatherapp.exceptionhandling.IncorrectInputDataException;
import com.epam.weatherapp.exceptionhandling.LocationAlreadyPresentException;
import com.epam.weatherapp.mapper.WeatherDetailsMapper;
import com.epam.weatherapp.service.WeatherService;

class WeatherControllerTest {

	@Mock
	WeatherService weatherService;
	Optional<WeatherDetailsDTO> weatherDetailsOptional;
	@Mock
	List<WeatherDetails> weatherDetailsList;
	@Spy
	WeatherDetailsDTO weatherDetailsDTO;
	@Mock
	WeatherDetailsMapper weatherDetailsMapper;
	
	WeatherDetails weatherDetails =new WeatherDetails();

	@InjectMocks
	WeatherController weatherController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetWeatherDetailsForSelectedCityPresentInDataBase() throws DefaultExceptionHandler {
		weatherDetailsOptional = Optional.of(new WeatherDetailsDTO());
		doReturn(weatherDetailsOptional).when(weatherService).getWeatherDetailsByLocationName(Mockito.anyString());
		doReturn(weatherDetailsDTO).when(weatherDetailsMapper).convertToDTO(weatherDetails);
		ResponseEntity<WeatherDetailsDTO> response = weatherController.getWeatherDetailsForSelectedCity("hyderabad");
		assertTrue(response.getStatusCode().equals(HttpStatus.FOUND));
		verify(weatherService).getWeatherDetailsByLocationName("hyderabad");
	}

	@Test
	void testGetWeatherDetailsForSelectedCityNotPresentInDatabase() throws DefaultExceptionHandler {
		weatherDetailsOptional = Optional.ofNullable(null);
		doReturn(weatherDetailsOptional).when(weatherService).getWeatherDetailsByLocationName(Mockito.anyString());
		doReturn(weatherDetailsDTO).when(weatherDetailsMapper).convertToDTO(weatherDetails);
		ResponseEntity<WeatherDetailsDTO> response = weatherController.getWeatherDetailsForSelectedCity("hyderabad");
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
		ResponseEntity<List<WeatherDetailsDTO>> response = weatherController.getWeatherDetailsForAllCities();
		assertTrue(response.getStatusCode().equals(HttpStatus.FOUND));
		verify(weatherService).getAllWeatherDetails();
	}

	@Test
	void testGetWeatherDetailsForAllCitiesNegative() throws DefaultExceptionHandler {
		doReturn(null).when(weatherService).getAllWeatherDetails();
		ResponseEntity<List<WeatherDetailsDTO>> response = weatherController.getWeatherDetailsForAllCities();
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
		doReturn(weatherDetailsDTO).when(weatherService).updateWeatherDetails(weatherDetailsDTO);
		doReturn(weatherDetailsDTO).when(weatherDetailsMapper).convertToDTO(weatherDetails);
		ResponseEntity<WeatherDetailsDTO> response = weatherController.addWeatherDetailsForSelectedCity(weatherDetailsDTO);
		assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
		verify(weatherService).updateWeatherDetails(weatherDetailsDTO);
	}

	@Test
	void testAddWeatherDetailsForSelectedCityNegative()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		doReturn(null).when(weatherService).updateWeatherDetails(weatherDetailsDTO);
		doReturn(weatherDetailsDTO).when(weatherDetailsMapper).convertToDTO(weatherDetails);
		ResponseEntity<WeatherDetailsDTO> response = weatherController.addWeatherDetailsForSelectedCity(weatherDetailsDTO);
		assertTrue(response.getStatusCode().equals(HttpStatus.NO_CONTENT));
		verify(weatherService).updateWeatherDetails(weatherDetailsDTO);
	}

	@Test
	void testAddWeatherDetailsForSelectedCityLocationAlreadyPresentException()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		weatherDetailsDTO.setLocationName("hyderabad");
		weatherDetailsDTO.setTemperature("24 degrees");
		doThrow(DataIntegrityViolationException.class).when(weatherService).updateWeatherDetails(weatherDetailsDTO);
		Exception e = assertThrows(LocationAlreadyPresentException.class, () -> {
			weatherController.addWeatherDetailsForSelectedCity(weatherDetailsDTO);
		});
		System.out.println(e.getMessage());
		assertTrue(e.getMessage().contains("Location Already Present"));
		verify(weatherService).updateWeatherDetails(weatherDetailsDTO);
	}
	@Test
	void testAddWeatherDetailsForSelectedCityIncorrectInputDataException()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		doThrow(ConstraintViolationException.class).when(weatherService).updateWeatherDetails(weatherDetailsDTO);
		Exception e = assertThrows(IncorrectInputDataException.class, () -> {
			weatherController.addWeatherDetailsForSelectedCity(weatherDetailsDTO);
		});
		System.out.println(e.getMessage());
		assertTrue(e.getMessage().contains("Incorrect Input Data"));
		verify(weatherService).updateWeatherDetails(weatherDetailsDTO);
	}
	@Test
	void testAddWeatherDetailsForSelectedCityDefaultExceptionHandler()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		doThrow(NullPointerException.class).when(weatherService).updateWeatherDetails(weatherDetailsDTO);
		Exception e = assertThrows(DefaultExceptionHandler.class, () -> {
			weatherController.addWeatherDetailsForSelectedCity(weatherDetailsDTO);
		});
		System.out.println(e.getMessage());
		assertTrue(e.getMessage().contains("Something went wrong"));
		verify(weatherService).updateWeatherDetails(weatherDetailsDTO);
	}
}
