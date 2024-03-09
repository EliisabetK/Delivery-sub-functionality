package com.example.trialtask;
import com.example.trialtask.objects.BaseFee;
import com.example.trialtask.objects.ExtraFee;
import com.example.trialtask.repositories.BaseFeeRepository;
import com.example.trialtask.repositories.ExtraFeeRepository;
import com.example.trialtask.services.DeliveryFeeServiceCRUD;
import com.example.trialtask.objects.WeatherData;
import com.example.trialtask.repositories.WeatherDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class DeliveryFeeServiceCRUDTest {

	@Mock
	private WeatherDataRepository weatherDataRepository;

	@Mock
	private BaseFeeRepository baseFeeRepository;

	@Mock
	private ExtraFeeRepository extraFeeRepository;

	@InjectMocks
	private DeliveryFeeServiceCRUD deliveryFeeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void calculateDeliveryFee_NoWeatherDataFound() {
		when(weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(any())).thenReturn(null);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> deliveryFeeService.calculateDeliveryFee("Tallinn", "bike")
		);

		assertEquals("No weather data found for the station: Tallinn-Harku", exception.getMessage());
	}

	@Test
	void calculateDeliveryFee_ExtraFeeForLowTemperature() {
		WeatherData weatherData = new WeatherData();
		weatherData.setAirTemperature(-15.0);
		weatherData.setWindSpeed(5.0);

		when(weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(any())).thenReturn(weatherData);
		when(baseFeeRepository.findByCityAndVehicleType(any(), any())).thenReturn(new BaseFee());
		when(extraFeeRepository.findByConditionTypeAndVehicleType("Air Temperature", "Bike"))
				.thenReturn(Collections.singletonList(new ExtraFee("Air Temperature", "< -10", 1.0, "bike")));

		double deliveryFee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Bike");

		assertEquals(1.0, deliveryFee);
	}

	@Test
	void calculateDeliveryFee_ExtraFeeForHighWindSpeed() {
		WeatherData weatherData = new WeatherData();
		weatherData.setAirTemperature(20.0);
		weatherData.setWindSpeed(15.0);
		weatherData.setPhenomenon("");

		when(weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(any())).thenReturn(weatherData);
		when(baseFeeRepository.findByCityAndVehicleType(any(), any())).thenReturn(new BaseFee());
		when(extraFeeRepository.findByConditionTypeAndVehicleType("Wind Speed", "Scooter"))
				.thenReturn(Collections.singletonList(new ExtraFee("Wind Speed", ">= 10 and < 20", 0.5, "Scooter")));

		double deliveryFee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Scooter");

		assertEquals(0.5, deliveryFee);
	}

	@Test
	void calculateDeliveryFee_ExtraFeeForTooHighWind() {
		WeatherData weatherData = new WeatherData();
		weatherData.setAirTemperature(20.0);
		weatherData.setWindSpeed(25.0);
		weatherData.setPhenomenon("");
		when(weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(any())).thenReturn(weatherData);
		when(baseFeeRepository.findByCityAndVehicleType(any(), any())).thenReturn(new BaseFee());
		when(extraFeeRepository.findByConditionTypeAndVehicleType("Wind Speed", "Scooter"))
				.thenReturn(Collections.singletonList(new ExtraFee("Wind Speed", "> 20", -1000, "Scooter")));

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> deliveryFeeService.calculateDeliveryFee("Tallinn", "Scooter")
		);
		assertEquals("Usage of selected vehicle type is forbidden.", exception.getMessage());
	}

	@Test
	void calculateDeliveryFee_ForbiddenWeather() {
		WeatherData weatherData = new WeatherData();
		weatherData.setAirTemperature(20.0);
		weatherData.setWindSpeed(5.0);
		weatherData.setPhenomenon("Glaze");
		when(weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(any())).thenReturn(weatherData);
		when(baseFeeRepository.findByCityAndVehicleType(any(), any())).thenReturn(new BaseFee());
		when(extraFeeRepository.findByConditionTypeAndVehicleType("Weather Phenomenon", "Scooter"))
				.thenReturn(Collections.singletonList(new ExtraFee("Weather Phenomenon", "Glaze", -1000, "Scooter")));

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> deliveryFeeService.calculateDeliveryFee("Tallinn", "Scooter")
		);
		assertEquals("Usage of selected vehicle type is forbidden.", exception.getMessage());
	}

	@Test
	void calculateDeliveryFee_ExtraFeeForWeatherPhenomenon() {
		WeatherData weatherData = new WeatherData();
		weatherData.setAirTemperature(20.0);
		weatherData.setWindSpeed(5.0);
		weatherData.setPhenomenon("Rain");
		when(weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(any())).thenReturn(weatherData);
		when(baseFeeRepository.findByCityAndVehicleType(any(), any())).thenReturn(new BaseFee());
		when(extraFeeRepository.findByConditionTypeAndVehicleType("Weather Phenomenon", "Bike"))
				.thenReturn(Collections.singletonList(new ExtraFee("Weather Phenomenon", "Rain", 0.5, "Bike")));

		double deliveryFee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Bike");
		assertEquals(0.5, deliveryFee);
	}

	@Test
	void calculateDeliveryFee_MultipleWeatherPhenomenons() {
		WeatherData weatherData = new WeatherData();
		weatherData.setAirTemperature(-13.0);
		weatherData.setWindSpeed(15.0);
		BaseFee baseFee = new BaseFee();
		baseFee.setFee(3.0);
		weatherData.setPhenomenon("Light snowfall");

		when(weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(any())).thenReturn(weatherData);
		when(baseFeeRepository.findByCityAndVehicleType(any(), any())).thenReturn(baseFee);

		List<ExtraFee> extraFees = new ArrayList<>();
		extraFees.add(new ExtraFee("Weather Phenomenon", "Snow or Sleet", 1.0, "Bike"));
		extraFees.add(new ExtraFee("Wind Speed", ">= 10 and < 20", 0.5, "Bike"));
		extraFees.add(new ExtraFee("Air Temperature", "< -10", 1.0, "Bike"));
		when(extraFeeRepository.findByConditionTypeAndVehicleType(any(), any())).thenReturn(extraFees);

		double deliveryFee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Bike");
		assertEquals(5.5, deliveryFee); // 3.0 + 1 + 0.5 + 1.0
	}

	@Test
	void calculateDeliveryFee_InvalidInputParameters() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> deliveryFeeService.calculateDeliveryFee(null, "car")
		);
		assertEquals("City cannot be null", exception.getMessage());

		exception = assertThrows(
				IllegalArgumentException.class,
				() -> deliveryFeeService.calculateDeliveryFee("Tallinn", null)
		);
		assertEquals("Vehicle type cannot be null", exception.getMessage());
	}

	@Test
	void calculateDeliveryFee_MissingWeatherData() {
		when(weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(any())).thenReturn(null);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> deliveryFeeService.calculateDeliveryFee("Tallinn", "car")
		);
		assertEquals("No weather data found for the station: Tallinn-Harku", exception.getMessage());
	}

	@Test
	void calculateDeliveryFee_ValidInputParameters() {
		WeatherData weatherData = new WeatherData();
		weatherData.setAirTemperature(10.0);
		weatherData.setWindSpeed(5.0);
		weatherData.setPhenomenon("");

		when(weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(any()))
				.thenReturn(weatherData);
		when(baseFeeRepository.findByCityAndVehicleType(any(), any())).thenReturn(new BaseFee());
		when(extraFeeRepository.findByConditionTypeAndVehicleType(any(), any())).thenReturn(Collections.emptyList());

		double deliveryFee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Bike");

		assertEquals(0.0, deliveryFee);
	}

	@Test
	void calculateDeliveryFee_InvalidCity() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> deliveryFeeService.calculateDeliveryFee("London", "Bike"));
		assertEquals("No weather data found for the station: London", exception.getMessage());
	}

	@Test
	void calculateDeliveryFee_InvalidVehicleType() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> deliveryFeeService.calculateDeliveryFee("Tallinn", "Airplane"));
		assertEquals("Invalid vehicle type: Airplane", exception.getMessage());
	}
}
