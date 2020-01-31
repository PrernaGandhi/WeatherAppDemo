package com.epam.weatherapp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.weatherapp.dto.WeatherDetailsDTO;
import com.epam.weatherapp.entity.WeatherDetails;

@Component
public class WeatherDetailsMapper {//check model mapper configuration
	@Autowired
    private ModelMapper modelMapper;
	
	public WeatherDetailsDTO convertToDTO(WeatherDetails weatherDetails) {
		return modelMapper.map(weatherDetails, WeatherDetailsDTO.class);
	}
	public WeatherDetails convertToEntity(WeatherDetailsDTO weatherDetailsDTO) {
		return modelMapper.map(weatherDetailsDTO, WeatherDetails.class);
	}
}
