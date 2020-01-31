package com.epam.weatherapp.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class SearchCriteria {
	private String countryName;
	private String stateName;
	private String locationName;

}
