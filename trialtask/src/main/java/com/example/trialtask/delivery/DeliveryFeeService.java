package com.example.trialtask.delivery;

import com.example.trialtask.weather.WeatherData;
import com.example.trialtask.weather.WeatherDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Service class for calculating delivery fees based on weather conditions and vehicle type.
 */
@Service
public class DeliveryFeeService {

    private final WeatherDataRepository weatherDataRepository;

    /**
     * Constructs a new DeliveryFeeService with the provided WeatherDataRepository.
     *
     * @param weatherDataRepository the WeatherDataRepository to be used for fetching weather data
     */
    public DeliveryFeeService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    /**
     * Calculates the delivery fee for the given city and vehicle type.
     *
     * @param city        the city for which the delivery fee is to be calculated
     * @param vehicleType the type of vehicle used for delivery (car, scooter, bike)
     * @return the calculated delivery fee
     * @throws IllegalArgumentException if no weather data is found for the specified city
     */
    public double calculateDeliveryFee(String city, String vehicleType) {
        if(city.equals("Tallinn"))
            city += "-Harku";
        else if(city.equals("Tartu"))
            city += "-Tõravere";

        WeatherData weatherData = weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(city);
        if (weatherData == null) {
            throw new IllegalArgumentException("No weather data found for the station: " + city);
        }
        return calculateDeliveryFeeExtracted(city, vehicleType, weatherData);
    }

    /**
     * Calculates the delivery fee for the given city, vehicle type, and date/time.
     *
     * @param city        the city for which the delivery fee is to be calculated
     * @param vehicleType the type of vehicle used for delivery (car, scooter, bike)
     * @param dateTime    the date and time for which the weather data is to be considered
     * @return the calculated delivery fee
     * @throws IllegalArgumentException if no weather data is found for the specified city and date/time
     */
    public double calculateDeliveryFee(String city, String vehicleType, LocalDateTime dateTime) {
        if(city.equals("Tallinn"))
            city += "-Harku";
        else if(city.equals("Tartu"))
            city += "-Tõravere";
        long observationTimestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
        WeatherData weatherData = weatherDataRepository.findByStationNameAndObservationTimestamp(city, observationTimestamp);
        if (weatherData == null) {
            throw new IllegalArgumentException("No weather data found for the station: " + city + " at the specified date and time.");
        }

        return calculateDeliveryFeeExtracted(city, vehicleType, weatherData);
    }

    /**
     * Calculates the delivery fee for the given city, vehicle type, and weather data.
     * This method is a helper method used by other public methods in this class.
     *
     * @param city         the city for which the delivery fee is to be calculated
     * @param vehicleType  the type of vehicle used for delivery (car, scooter, bike)
     * @param weatherData  the weather data for the specified city
     * @return the calculated delivery fee
     */

    private double calculateDeliveryFeeExtracted(String city, String vehicleType, WeatherData weatherData) {
        double rbf = findRegionalBaseFee(city, vehicleType);
        double atef = calculateAirTemperatureExtraFee(vehicleType, weatherData.getAirTemperature());
        double wsef = calculateWindSpeedExtraFee(vehicleType, weatherData.getWindSpeed());
        double wpef = 0;
        if (weatherData.getPhenomenon() != null) {
            wpef = calculateWeatherPhenomenonExtraFee(vehicleType, weatherData.getPhenomenon());
        }
        return rbf + atef + wsef + wpef;
    }
    private double findRegionalBaseFee(String city, String vehicleType) {
        if (city.equalsIgnoreCase("Tallinn-Harku") || city.equalsIgnoreCase("Tallinn")) {
            switch (vehicleType.toLowerCase()) {
                case "car":
                    return 4.0;
                case "scooter":
                    return 3.5;
                case "bike":
                    return 3.0;
            }
        } else if (city.equalsIgnoreCase("Tartu-Tõravere")|| city.equalsIgnoreCase("Tartu")) {
            switch (vehicleType.toLowerCase()) {
                case "car":
                    return 3.5;
                case "scooter":
                    return 3.0;
                case "bike":
                    return 2.5;
            }
        } else if (city.equalsIgnoreCase("Pärnu")) {
            switch (vehicleType.toLowerCase()) {
                case "car":
                    return 3;
                case "scooter":
                    return 2.5;
                case "bike":
                    return 2.0;
            }
        }
        throw new IllegalArgumentException("Invalid city or vehicle type");
    }
    private double calculateAirTemperatureExtraFee(String vehicleType, double temperature) {
        if (vehicleType.equalsIgnoreCase("scooter") || vehicleType.equalsIgnoreCase("bike")) {
            if (temperature < -10) {
                return 1.0;
            } else if (temperature < 0) {
                return 0.5;
            }
        }
        return 0;
    }

    private double calculateWindSpeedExtraFee(String vehicleType, double windSpeed) {
        if (vehicleType.equalsIgnoreCase("bike")) {
            if (windSpeed > 20) {
                throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
            } else if (windSpeed > 10) {
                return 0.5;
            }
        }
        return 0;
    }

    private double calculateWeatherPhenomenonExtraFee(String vehicleType, String weatherPhenomenon) { // Calculates the WPEF according to the business rules
        if (vehicleType.equalsIgnoreCase("scooter") || vehicleType.equalsIgnoreCase("bike")) {
            if (weatherPhenomenon.contains("snow") || weatherPhenomenon.contains("sleet")) {
                return 1.0;
            } else if (weatherPhenomenon.contains("rain")) {
                return 0.5;
            } else if (weatherPhenomenon.contains("glaze") || weatherPhenomenon.contains("hail") || weatherPhenomenon.contains("thunder")) {
                throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
            }
        }
        return 0;
    }
}
