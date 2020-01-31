package com.epam.weatherapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.epam.weatherapp.entity.WeatherDetails;

@Repository
public interface WeatherDetailsRepository
		extends CrudRepository<WeatherDetails, Integer> {

	Optional<WeatherDetails> findByLocationNameIgnoreCase(@Param("location_name") String locationName);

	long deleteByLocationName(String city);

	List<WeatherDetails> findByCountryNameAndLocationNameAndStateName(String countryName, String locationName,
			String stateName);

	List<WeatherDetails> findByCountryNameAndLocationName(String countryName, String locationName);

	List<WeatherDetails> findByCountryNameAndStateName(String countryName, String stateName);

	List<WeatherDetails> findByLocationNameAndStateName(String locationName, String stateName);

	List<WeatherDetails> findByLocationName(String locationName);

	List<WeatherDetails> findByCountryName(String countryName);

	List<WeatherDetails> findByStateName(String stateName);

}
