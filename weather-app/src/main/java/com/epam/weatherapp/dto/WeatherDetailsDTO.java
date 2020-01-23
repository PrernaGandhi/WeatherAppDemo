package com.epam.weatherapp.dto;

import org.springframework.stereotype.Component;

import lombok.Data;


@Data
@Component
public class WeatherDetailsDTO {

	private String locationName;

	private String temperature;
}
