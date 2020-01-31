package com.epam.weatherapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
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

import com.epam.weatherapp.dto.SearchCriteria;
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

	WeatherDetails weatherDetails = new WeatherDetails();

	@InjectMocks
	WeatherController weatherController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testAddWeatherDetailsForSelectedCityPositive()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		doReturn(weatherDetailsDTO).when(weatherService).addWeatherDetails(weatherDetailsDTO);
		doReturn(weatherDetailsDTO).when(weatherDetailsMapper).convertToDTO(weatherDetails);
		ResponseEntity<WeatherDetailsDTO> response = weatherController
				.addWeatherDetailsForSelectedCity(weatherDetailsDTO);
		assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
		verify(weatherService).addWeatherDetails(weatherDetailsDTO);
	}

	@Test
	void testAddWeatherDetailsForSelectedCityNegative()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		doReturn(null).when(weatherService).addWeatherDetails(weatherDetailsDTO);
		doReturn(weatherDetailsDTO).when(weatherDetailsMapper).convertToDTO(weatherDetails);
		ResponseEntity<WeatherDetailsDTO> response = weatherController
				.addWeatherDetailsForSelectedCity(weatherDetailsDTO);
		assertTrue(response.getStatusCode().equals(HttpStatus.NO_CONTENT));
		verify(weatherService).addWeatherDetails(weatherDetailsDTO);
	}

	@Test
	void testAddWeatherDetailsForSelectedCityLocationAlreadyPresentException()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		weatherDetailsDTO.setLocationName("hyderabad");
		weatherDetailsDTO.setTemperature("24 degrees");
		doThrow(DataIntegrityViolationException.class).when(weatherService).addWeatherDetails(weatherDetailsDTO);
		Exception e = assertThrows(LocationAlreadyPresentException.class, () -> {
			weatherController.addWeatherDetailsForSelectedCity(weatherDetailsDTO);
		});
		assertTrue(e.getMessage().contains("Location Already Present"));
		verify(weatherService).addWeatherDetails(weatherDetailsDTO);
	}

	@Test
	void testAddWeatherDetailsForSelectedCityIncorrectInputDataException()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		doThrow(ConstraintViolationException.class).when(weatherService).addWeatherDetails(weatherDetailsDTO);
		Exception e = assertThrows(IncorrectInputDataException.class, () -> {
			weatherController.addWeatherDetailsForSelectedCity(weatherDetailsDTO);
		});
		assertTrue(e.getMessage().contains("Incorrect Input Data"));
		verify(weatherService).addWeatherDetails(weatherDetailsDTO);
	}

	@Test
	void testAddWeatherDetailsForSelectedCityDefaultExceptionHandler()
			throws LocationAlreadyPresentException, IncorrectInputDataException, DefaultExceptionHandler {
		doThrow(NullPointerException.class).when(weatherService).addWeatherDetails(weatherDetailsDTO);
		Exception e = assertThrows(DefaultExceptionHandler.class, () -> {
			weatherController.addWeatherDetailsForSelectedCity(weatherDetailsDTO);
		});
		assertTrue(e.getMessage().contains("Something went wrong"));
		verify(weatherService).addWeatherDetails(weatherDetailsDTO);
	}

	@Test
	void testGetWeatherDetailsPositive() throws DefaultExceptionHandler {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setCountryName("");
		searchCriteria.setLocationName("");
		searchCriteria.setStateName("");
		WeatherDetails weatherDetailsCity1 = new WeatherDetails();
		weatherDetailsCity1.setLocationName("hyderabad");
		weatherDetailsCity1.setTemperature("24 Degrees");
		WeatherDetails weatherDetailsCity2 = new WeatherDetails();
		weatherDetailsCity2.setLocationName("bangalore");
		weatherDetailsCity2.setTemperature("20 Degrees");
		List<WeatherDetailsDTO> weatherDetailsDTOsList = Arrays.asList(
				weatherDetailsMapper.convertToDTO(weatherDetailsCity1),
				weatherDetailsMapper.convertToDTO(weatherDetailsCity2));
		doReturn(weatherDetailsDTOsList).when(weatherService).getAllWeatherDetails(searchCriteria);
		ResponseEntity<List<WeatherDetailsDTO>> response = weatherController.getWeatherDetails(searchCriteria);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(weatherDetailsDTOsList, response.getBody());
	}

	@Test
	void testGetWeatherDetailsNegative() throws DefaultExceptionHandler {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setCountryName("");
		searchCriteria.setLocationName("");
		searchCriteria.setStateName("");
		doReturn(null).when(weatherService).getAllWeatherDetails(searchCriteria);
		ResponseEntity<List<WeatherDetailsDTO>> response = weatherController.getWeatherDetails(searchCriteria);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	void testGetWeatherDetailsDefaultExceptionHandler() throws DefaultExceptionHandler {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setCountryName("");
		searchCriteria.setLocationName("");
		searchCriteria.setStateName("");
		doThrow(NullPointerException.class).when(weatherService).getAllWeatherDetails(searchCriteria);
		Exception e = assertThrows(DefaultExceptionHandler.class, () -> {
			weatherController.getWeatherDetails(searchCriteria);
		});
		assertTrue(e.getMessage().contains("Something went wrong"));
		verify(weatherService).getAllWeatherDetails(searchCriteria);
	}

	@Test
	void testDeleteWeatherDetailsCityPresentInDatabaseAndDeleteSuccessful() throws DefaultExceptionHandler {
		weatherDetailsOptional = Optional.of(new WeatherDetailsDTO());
		doReturn(weatherDetailsOptional).when(weatherService).getWeatherDetailsByLocationName(Mockito.anyString());
		doReturn(true).when(weatherService).deleteByLocationName(Mockito.anyString());
		ResponseEntity<String> response = weatherController.deleteWeatherDetails("");
		assertEquals("Record Deleted Successfully!!", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	void testDeleteWeatherDetailsCityPresentInDatabaseAndDeleteUnSuccessful() throws DefaultExceptionHandler {
		weatherDetailsOptional = Optional.of(new WeatherDetailsDTO());
		doReturn(weatherDetailsOptional).when(weatherService).getWeatherDetailsByLocationName(Mockito.anyString());
		doReturn(false).when(weatherService).deleteByLocationName(Mockito.anyString());
		ResponseEntity<String> response = weatherController.deleteWeatherDetails("");
		assertEquals("Unable to delete Record!!", response.getBody());
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

	}

	@Test
	void testDeleteWeatherDetailsCityNotPresentInDatabase() throws DefaultExceptionHandler {
		weatherDetailsOptional = Optional.ofNullable(null);
		doReturn(weatherDetailsOptional).when(weatherService).getWeatherDetailsByLocationName(Mockito.anyString());
		doReturn(false).when(weatherService).deleteByLocationName(Mockito.anyString());
		ResponseEntity<String> response = weatherController.deleteWeatherDetails("");
		assertEquals("City not available in database to delete!!", response.getBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}

	@Test
	void testDeleteWeatherDetailsCityDefaultExceptionHandler() throws DefaultExceptionHandler {
		doThrow(NullPointerException.class).when(weatherService).getWeatherDetailsByLocationName(Mockito.anyString());
		Exception e = assertThrows(DefaultExceptionHandler.class, () -> {
			weatherController.deleteWeatherDetails("");
		});
		assertTrue(e.getMessage().contains("Something went wrong"));
		verify(weatherService).getWeatherDetailsByLocationName("");
	}
}
