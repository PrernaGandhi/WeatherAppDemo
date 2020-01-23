package com.epam.weatherapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.epam.weatherapp.dto.WeatherDetailsDTO;
import com.epam.weatherapp.entity.WeatherDetails;
import com.epam.weatherapp.mapper.WeatherDetailsMapper;
import com.epam.weatherapp.repository.WeatherDetailsRepository;



@Service
public class WeatherService {
	@Autowired
	WeatherDetailsRepository weatherDetailsRepository;
	@Autowired
	WeatherDetailsMapper weatherDetailsMapper;
	
	public Optional<WeatherDetailsDTO> getWeatherDetailsByLocationName(String locationName) {	
		Optional<WeatherDetails> weatherDetails = weatherDetailsRepository.findByLocationNameIgnoreCase(locationName);
		WeatherDetailsDTO weatherDetailsDTO = null;
		if(weatherDetails.isPresent()) {
			weatherDetailsDTO = weatherDetailsMapper.convertToDTO(weatherDetails.get());
		}
		return Optional.ofNullable(weatherDetailsDTO);
	}
	public WeatherDetailsDTO updateWeatherDetails(WeatherDetailsDTO weatherDetailsDTO) {
		WeatherDetails weatherDetails = weatherDetailsMapper.convertToEntity(weatherDetailsDTO);
		 weatherDetails = weatherDetailsRepository.save(weatherDetails);
		 weatherDetailsDTO = weatherDetailsMapper.convertToDTO(weatherDetails);
		return weatherDetailsDTO;
	}
	public List<WeatherDetailsDTO> getAllWeatherDetails() {
		List<WeatherDetails> weatherDetailsList = (List<WeatherDetails>) weatherDetailsRepository.findAll();
		return weatherDetailsList.stream().map(e -> weatherDetailsMapper.convertToDTO(e)).collect(Collectors.toList());
	}

}
