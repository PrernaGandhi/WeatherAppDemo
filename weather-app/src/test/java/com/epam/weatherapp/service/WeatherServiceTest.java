package com.epam.weatherapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.epam.weatherapp.entity.WeatherDetails;
import com.epam.weatherapp.repository.WeatherDetailsRepository;

class WeatherServiceTest {

	@Mock
	WeatherDetailsRepository weatherDetailsRepository;
	@InjectMocks
	WeatherService weatherService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testGetWeatherDetailsByLocationame() {
		Optional<WeatherDetails> weatherDetails = Optional.of(new WeatherDetails());
		doReturn(weatherDetails).when(weatherDetailsRepository).findByLocationNameIgnoreCase(Mockito.anyString());
		assertEquals(weatherDetails, weatherService.getWeatherDetailsByLocationName("hyderabad"));
	}

	@Test
	void testUpdateWeatherDetails() {
		WeatherDetails weatherDetails = new WeatherDetails();
		doReturn(weatherDetails).when(weatherDetailsRepository).save(weatherDetails);
		assertEquals(weatherDetails, weatherService.updateWeatherDetails(weatherDetails));
	}
	@Test
	void testGetAllWeatherDetails() {
		List<WeatherDetails> weatherDetails = Arrays.asList(new WeatherDetails());
		doReturn(weatherDetails).when(weatherDetailsRepository).findAll();
		assertEquals(weatherDetails, weatherService.getAllWeatherDetails());
	}
}
