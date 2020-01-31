package com.epam.weatherapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.weatherapp.dto.SearchCriteria;
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
		if (weatherDetails.isPresent()) {
			weatherDetailsDTO = weatherDetailsMapper.convertToDTO(weatherDetails.get());
		}
		return Optional.ofNullable(weatherDetailsDTO);
	}

	public WeatherDetailsDTO addWeatherDetails(WeatherDetailsDTO weatherDetailsDTO) {
		WeatherDetails weatherDetails = weatherDetailsMapper.convertToEntity(weatherDetailsDTO);
		weatherDetails = weatherDetailsRepository.save(weatherDetails);
		weatherDetailsDTO = weatherDetailsMapper.convertToDTO(weatherDetails);
		return weatherDetailsDTO;
	}

	public List<WeatherDetailsDTO> getAllWeatherDetails(SearchCriteria searchCriteria) {
		List<WeatherDetails> weatherDetailsList = null;
		if (searchCriteria == null) {
			weatherDetailsList = (List<WeatherDetails>) weatherDetailsRepository.findAll();
		} else {
			if (searchByCountryNameLocationNameAndStateName(searchCriteria)) {
				weatherDetailsList = weatherDetailsRepository.findByCountryNameAndLocationNameAndStateName(
						searchCriteria.getCountryName(), searchCriteria.getLocationName(),
						searchCriteria.getStateName());
			} else if (searchByCountryNameAndLocationName(searchCriteria)) {
				weatherDetailsList = weatherDetailsRepository.findByCountryNameAndLocationName(
						searchCriteria.getCountryName(), searchCriteria.getLocationName());

			} else if (searchByCountryNameAndStateName(searchCriteria)) {
				weatherDetailsList = weatherDetailsRepository
						.findByCountryNameAndStateName(searchCriteria.getCountryName(), searchCriteria.getStateName());

			} else if (searchByLocationNameAndStateName(searchCriteria)) {
				weatherDetailsList = weatherDetailsRepository.findByLocationNameAndStateName(
						searchCriteria.getLocationName(), searchCriteria.getStateName());
			} else if (searchCriteria.getLocationName() != null) {
				weatherDetailsList = weatherDetailsRepository.findByLocationName(searchCriteria.getLocationName());

			} else if (searchCriteria.getCountryName() != null) {
				weatherDetailsList = weatherDetailsRepository.findByCountryName(searchCriteria.getCountryName());
			} else {
				weatherDetailsList = weatherDetailsRepository.findByStateName(searchCriteria.getStateName());
			}

		}
		return weatherDetailsList.stream().map(e -> weatherDetailsMapper.convertToDTO(e)).collect(Collectors.toList());
	}

	private boolean searchByLocationNameAndStateName(SearchCriteria searchCriteria) {
		return searchCriteria.getLocationName() != null && searchCriteria.getStateName() != null;
	}

	private boolean searchByCountryNameAndStateName(SearchCriteria searchCriteria) {
		return searchCriteria.getCountryName() != null && searchCriteria.getStateName() != null;
	}

	private boolean searchByCountryNameAndLocationName(SearchCriteria searchCriteria) {
		return searchCriteria.getCountryName() != null && searchCriteria.getLocationName() != null;
	}

	private boolean searchByCountryNameLocationNameAndStateName(SearchCriteria searchCriteria) {
		return searchByCountryNameAndLocationName(searchCriteria) && searchCriteria.getStateName() != null;
	}

	public boolean deleteByLocationName(String city) {
		long noOfRecordsDeleted = weatherDetailsRepository.deleteByLocationName(city);
		boolean isSuccess = false;
		if (noOfRecordsDeleted > 0)
			isSuccess = true;
		return isSuccess;
	}

}
