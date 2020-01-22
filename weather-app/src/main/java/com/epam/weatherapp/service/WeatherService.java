package com.epam.weatherapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.weatherapp.entity.WeatherDetails;
import com.epam.weatherapp.repository.WeatherDetailsRepository;



@Service
public class WeatherService {
	@Autowired
	WeatherDetailsRepository weatherDetailsRepository;
	
	public Optional<WeatherDetails> getWeatherDetailsByLocationName(String locationName) {		
		return weatherDetailsRepository.findByLocationNameIgnoreCase(locationName);
	}
	public WeatherDetails updateWeatherDetails(WeatherDetails weatherDetails) {
		return weatherDetailsRepository.save(weatherDetails);
	}
	public List<WeatherDetails> getAllWeatherDetails() {
		return (List<WeatherDetails>) weatherDetailsRepository.findAll();
	}

}
