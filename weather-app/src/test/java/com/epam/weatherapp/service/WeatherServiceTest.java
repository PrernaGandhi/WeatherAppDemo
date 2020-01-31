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

import com.epam.weatherapp.dto.SearchCriteria;
import com.epam.weatherapp.dto.WeatherDetailsDTO;
import com.epam.weatherapp.entity.WeatherDetails;
import com.epam.weatherapp.mapper.WeatherDetailsMapper;
import com.epam.weatherapp.repository.WeatherDetailsRepository;

class WeatherServiceTest {

	@Mock
	WeatherDetailsRepository weatherDetailsRepository;
	@Mock
	WeatherDetailsDTO weatherDetailsDTO;
	@Mock
	WeatherDetailsMapper weatherDetailsMapper;
	@InjectMocks
	WeatherService weatherService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetWeatherDetailsByLocationamePositive() {
		Optional<WeatherDetails> weatherDetails = Optional.of(new WeatherDetails());
		doReturn(weatherDetails).when(weatherDetailsRepository).findByLocationNameIgnoreCase(Mockito.anyString());
		doReturn(weatherDetailsDTO).when(weatherDetailsMapper).convertToDTO(weatherDetails.get());
		assertEquals(Optional.of(weatherDetailsDTO), weatherService.getWeatherDetailsByLocationName("hyderabad"));
	}

	@Test
	void testGetWeatherDetailsByLocationameNegative() {
		Optional<WeatherDetails> weatherDetails = Optional.ofNullable(null);
		doReturn(weatherDetails).when(weatherDetailsRepository).findByLocationNameIgnoreCase(Mockito.anyString());
		assertEquals(Optional.empty(), weatherService.getWeatherDetailsByLocationName("hyderabad"));
	}

	@Test
	void testUpdateWeatherDetailsPositive() {
		WeatherDetails weatherDetails = new WeatherDetails();
		doReturn(weatherDetails).when(weatherDetailsRepository).save(weatherDetails);
		doReturn(weatherDetails).when(weatherDetailsMapper).convertToEntity(weatherDetailsDTO);
		doReturn(weatherDetailsDTO).when(weatherDetailsMapper).convertToDTO(weatherDetails);
		assertEquals(weatherDetailsDTO, weatherService.addWeatherDetails(weatherDetailsDTO));
	}

	@Test
	void testDeleteByLocationNamePositive() {
		doReturn(1L).when(weatherDetailsRepository).deleteByLocationName(Mockito.anyString());
		assertTrue(weatherService.deleteByLocationName("hyderabad"));
	}

	@Test
	void testDeleteByLocationNameNegative() {
		doReturn(0L).when(weatherDetailsRepository).deleteByLocationName(Mockito.anyString());
		assertFalse(weatherService.deleteByLocationName("hyderabad"));
	}

	@Test
	void testGetAllWeatherDetailsWithNoSearchCriteria() {
		WeatherDetails weatherDetailsCity1 = new WeatherDetails();
		weatherDetailsCity1.setLocationName("hyderabad");
		weatherDetailsCity1.setTemperature("24 Degrees");
		WeatherDetails weatherDetailsCity2 = new WeatherDetails();
		weatherDetailsCity2.setLocationName("bangalore");
		weatherDetailsCity2.setTemperature("20 Degrees");
		List<WeatherDetails> weatherDetailsList = Arrays.asList(weatherDetailsCity1, weatherDetailsCity2);
		List<WeatherDetailsDTO> weatherDetailsDTOsList = Arrays.asList(
				weatherDetailsMapper.convertToDTO(weatherDetailsCity1),
				weatherDetailsMapper.convertToDTO(weatherDetailsCity2));
		doReturn(weatherDetailsList).when(weatherDetailsRepository).findAll();
		assertEquals(weatherDetailsDTOsList, weatherService.getAllWeatherDetails(null));
	}

	@Test
	void testGetAllWeatherDetailsWithSearchCriteriaAsCountryNameStateNameAndLocationName() {
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
		List<WeatherDetails> weatherDetailsList = Arrays.asList(weatherDetailsCity1, weatherDetailsCity2);
		List<WeatherDetailsDTO> weatherDetailsDTOsList = Arrays.asList(
				weatherDetailsMapper.convertToDTO(weatherDetailsCity1),
				weatherDetailsMapper.convertToDTO(weatherDetailsCity2));
		doReturn(weatherDetailsList).when(weatherDetailsRepository).findByCountryNameAndLocationNameAndStateName(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		assertEquals(weatherDetailsDTOsList, weatherService.getAllWeatherDetails(searchCriteria));
	}

	@Test
	void testGetAllWeatherDetailsWithSearchCriteriaAsStateNameAndLocationName() {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setCountryName(null);
		searchCriteria.setLocationName("");
		searchCriteria.setStateName("");
		WeatherDetails weatherDetailsCity1 = new WeatherDetails();
		weatherDetailsCity1.setLocationName("hyderabad");
		weatherDetailsCity1.setTemperature("24 Degrees");
		WeatherDetails weatherDetailsCity2 = new WeatherDetails();
		weatherDetailsCity2.setLocationName("bangalore");
		weatherDetailsCity2.setTemperature("20 Degrees");
		List<WeatherDetails> weatherDetailsList = Arrays.asList(weatherDetailsCity1, weatherDetailsCity2);
		List<WeatherDetailsDTO> weatherDetailsDTOsList = Arrays.asList(
				weatherDetailsMapper.convertToDTO(weatherDetailsCity1),
				weatherDetailsMapper.convertToDTO(weatherDetailsCity2));
		doReturn(weatherDetailsList).when(weatherDetailsRepository).findByLocationNameAndStateName(Mockito.anyString(),
				Mockito.anyString());
		assertEquals(weatherDetailsDTOsList, weatherService.getAllWeatherDetails(searchCriteria));
	}

	@Test
	void testGetAllWeatherDetailsWithSearchCriteriaAsCountryNameAndLocationName() {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setCountryName("");
		searchCriteria.setLocationName("");
		searchCriteria.setStateName(null);
		WeatherDetails weatherDetailsCity1 = new WeatherDetails();
		weatherDetailsCity1.setLocationName("hyderabad");
		weatherDetailsCity1.setTemperature("24 Degrees");
		WeatherDetails weatherDetailsCity2 = new WeatherDetails();
		weatherDetailsCity2.setLocationName("bangalore");
		weatherDetailsCity2.setTemperature("20 Degrees");
		List<WeatherDetails> weatherDetailsList = Arrays.asList(weatherDetailsCity1, weatherDetailsCity2);
		List<WeatherDetailsDTO> weatherDetailsDTOsList = Arrays.asList(
				weatherDetailsMapper.convertToDTO(weatherDetailsCity1),
				weatherDetailsMapper.convertToDTO(weatherDetailsCity2));
		doReturn(weatherDetailsList).when(weatherDetailsRepository)
				.findByCountryNameAndLocationName(Mockito.anyString(), Mockito.anyString());
		assertEquals(weatherDetailsDTOsList, weatherService.getAllWeatherDetails(searchCriteria));
	}

	@Test
	void testGetAllWeatherDetailsWithSearchCriteriaAsCountryNameAndStateName() {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setCountryName("");
		searchCriteria.setLocationName(null);
		searchCriteria.setStateName("");
		WeatherDetails weatherDetailsCity1 = new WeatherDetails();
		weatherDetailsCity1.setLocationName("hyderabad");
		weatherDetailsCity1.setTemperature("24 Degrees");
		WeatherDetails weatherDetailsCity2 = new WeatherDetails();
		weatherDetailsCity2.setLocationName("bangalore");
		weatherDetailsCity2.setTemperature("20 Degrees");
		List<WeatherDetails> weatherDetailsList = Arrays.asList(weatherDetailsCity1, weatherDetailsCity2);
		List<WeatherDetailsDTO> weatherDetailsDTOsList = Arrays.asList(
				weatherDetailsMapper.convertToDTO(weatherDetailsCity1),
				weatherDetailsMapper.convertToDTO(weatherDetailsCity2));
		doReturn(weatherDetailsList).when(weatherDetailsRepository).findByCountryNameAndStateName(Mockito.anyString(),
				Mockito.anyString());
		assertEquals(weatherDetailsDTOsList, weatherService.getAllWeatherDetails(searchCriteria));
	}

	@Test
	void testGetAllWeatherDetailsWithSearchCriteriaAsCountryName() {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setCountryName("");
		searchCriteria.setLocationName(null);
		searchCriteria.setStateName(null);
		WeatherDetails weatherDetailsCity1 = new WeatherDetails();
		weatherDetailsCity1.setLocationName("hyderabad");
		weatherDetailsCity1.setTemperature("24 Degrees");
		WeatherDetails weatherDetailsCity2 = new WeatherDetails();
		weatherDetailsCity2.setLocationName("bangalore");
		weatherDetailsCity2.setTemperature("20 Degrees");
		List<WeatherDetails> weatherDetailsList = Arrays.asList(weatherDetailsCity1, weatherDetailsCity2);
		List<WeatherDetailsDTO> weatherDetailsDTOsList = Arrays.asList(
				weatherDetailsMapper.convertToDTO(weatherDetailsCity1),
				weatherDetailsMapper.convertToDTO(weatherDetailsCity2));
		doReturn(weatherDetailsList).when(weatherDetailsRepository).findByCountryName(Mockito.anyString());
		assertEquals(weatherDetailsDTOsList, weatherService.getAllWeatherDetails(searchCriteria));
	}

	@Test
	void testGetAllWeatherDetailsWithSearchCriteriaAsStateName() {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setCountryName(null);
		searchCriteria.setLocationName(null);
		searchCriteria.setStateName("");
		WeatherDetails weatherDetailsCity1 = new WeatherDetails();
		weatherDetailsCity1.setLocationName("hyderabad");
		weatherDetailsCity1.setTemperature("24 Degrees");
		WeatherDetails weatherDetailsCity2 = new WeatherDetails();
		weatherDetailsCity2.setLocationName("bangalore");
		weatherDetailsCity2.setTemperature("20 Degrees");
		List<WeatherDetails> weatherDetailsList = Arrays.asList(weatherDetailsCity1, weatherDetailsCity2);
		List<WeatherDetailsDTO> weatherDetailsDTOsList = Arrays.asList(
				weatherDetailsMapper.convertToDTO(weatherDetailsCity1),
				weatherDetailsMapper.convertToDTO(weatherDetailsCity2));
		doReturn(weatherDetailsList).when(weatherDetailsRepository).findByStateName(Mockito.anyString());
		assertEquals(weatherDetailsDTOsList, weatherService.getAllWeatherDetails(searchCriteria));
	}

	@Test
	void testGetAllWeatherDetailsWithSearchCriteriaAsLocationName() {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setCountryName(null);
		searchCriteria.setLocationName("");
		searchCriteria.setStateName(null);
		WeatherDetails weatherDetailsCity1 = new WeatherDetails();
		weatherDetailsCity1.setLocationName("hyderabad");
		weatherDetailsCity1.setTemperature("24 Degrees");
		WeatherDetails weatherDetailsCity2 = new WeatherDetails();
		weatherDetailsCity2.setLocationName("bangalore");
		weatherDetailsCity2.setTemperature("20 Degrees");
		List<WeatherDetails> weatherDetailsList = Arrays.asList(weatherDetailsCity1, weatherDetailsCity2);
		List<WeatherDetailsDTO> weatherDetailsDTOsList = Arrays.asList(
				weatherDetailsMapper.convertToDTO(weatherDetailsCity1),
				weatherDetailsMapper.convertToDTO(weatherDetailsCity2));
		doReturn(weatherDetailsList).when(weatherDetailsRepository).findByLocationName(Mockito.anyString());
		assertEquals(weatherDetailsDTOsList, weatherService.getAllWeatherDetails(searchCriteria));
	}
}
