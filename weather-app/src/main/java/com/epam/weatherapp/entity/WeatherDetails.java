package com.epam.weatherapp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.stereotype.Component;

import lombok.Data;

@Entity
@Data
@Component
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "locationName"))
public class WeatherDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Pattern(regexp = "[A-z]+")
	@NotBlank
	private String locationName;

	@Pattern(regexp = "[A-z]+")
	@NotBlank
	private String countryName;

	@Pattern(regexp = "[A-z]+")
	@NotBlank
	private String stateName;

	@Pattern(regexp = "[A-z0-9 ]+")
	@NotBlank
	private String temperature;

	@Pattern(regexp = "[0-9]+%")
	@NotBlank
	private String humidity;

	@PrePersist
	private void prepare() {
		this.locationName = locationName != null ? locationName.toLowerCase() : null;
		this.countryName = countryName != null ? countryName.toLowerCase() : null;
		this.stateName = stateName != null ? stateName.toLowerCase() : null;
	}

}
