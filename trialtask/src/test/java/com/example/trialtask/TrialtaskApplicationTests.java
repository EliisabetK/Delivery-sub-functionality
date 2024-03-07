package com.example.trialtask;
import com.example.trialtask.feesCRUD.*;
import com.example.trialtask.weather.WeatherData;
import com.example.trialtask.weather.WeatherDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/*TODO:
 * 1. Test for calculating delivery fee with valid input parameters
 * 2. Test for calculating delivery fee with invalid input parameters:
 * 3. Test for handling missing weather data:
 * 4. Test for CronJob functionality
 * 5. Test for error handling in CronJob
 * 6. Test for CRUD operations on base fees and extra fees
 * 7. Test for REST interface
 * 8. Test for datetime parameter
 */

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
		weatherData.setPhenomenon("Clear");

		when(weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(any())).thenReturn(weatherData);
		when(baseFeeRepository.findByCityAndVehicleType(any(), any())).thenReturn(new BaseFee());
		when(extraFeeRepository.findByConditionTypeAndVehicleType("Wind Speed", "Scooter"))
				.thenReturn(Collections.singletonList(new ExtraFee("Wind Speed", ">= 10 and < 20", 0.5, "Scooter")));

		double deliveryFee = deliveryFeeService.calculateDeliveryFee("Tallinn", "Scooter");

		assertEquals(0.5, deliveryFee);
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
