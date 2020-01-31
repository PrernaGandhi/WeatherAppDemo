package com.epam.weatherapp.dto;

import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Component
@ApiModel(description = "Details of weather ")
public class WeatherDetailsDTO {

	@ApiModelProperty(notes = "City name")
	private String locationName;
	@ApiModelProperty(notes = "Temperature")
	private String temperature;
	@ApiModelProperty(notes = "Country name")
	private String countryName;
	@ApiModelProperty(notes = "State name")
	private String stateName;
	@ApiModelProperty(notes = "Humidity")
	private String humidity;

}
