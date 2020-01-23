package com.epam.weatherapp.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import com.epam.weatherapp.dto.WeatherDetailsDTO;
import com.epam.weatherapp.entity.WeatherDetails;

class WeatherDetailsMapperTest {

	@InjectMocks
	WeatherDetailsMapper weatherDetailsMapper;
	@Mock
	WeatherDetails weatherDetails ;
	@Mock
	WeatherDetailsDTO weatherDetailsDTO;
	@Mock
	ModelMapper modelMapper;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testConvertToDTOPositive() {
		doReturn(weatherDetailsDTO).when(modelMapper).map(Mockito.any(),Mockito.any());
		WeatherDetailsDTO weatherDetailsDTO = weatherDetailsMapper.convertToDTO(weatherDetails);
		assertEquals(weatherDetails.getLocationName(), weatherDetailsDTO.getLocationName());
		assertEquals(weatherDetails.getTemperature(), weatherDetailsDTO.getTemperature());
	}
	
	@Test
	void testConvertToEntityPositive() {
		doReturn(weatherDetails).when(modelMapper).map(Mockito.any(),Mockito.any());
		WeatherDetails weatherDetails = weatherDetailsMapper.convertToEntity(weatherDetailsDTO);
		assertEquals(weatherDetailsDTO.getLocationName(), weatherDetails.getLocationName());
		assertEquals(weatherDetailsDTO.getTemperature(), weatherDetails.getTemperature());
	}

}
