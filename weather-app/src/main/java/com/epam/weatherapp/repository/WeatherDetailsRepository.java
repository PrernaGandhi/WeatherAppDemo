package com.epam.weatherapp.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.epam.weatherapp.entity.WeatherDetails;

@Repository
public interface WeatherDetailsRepository extends CrudRepository<WeatherDetails, Integer> {

	Optional<WeatherDetails> findByLocationNameIgnoreCase(@Param("location_name") String locationName);
	
}
