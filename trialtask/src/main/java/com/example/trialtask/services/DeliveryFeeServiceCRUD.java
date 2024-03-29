package com.example.trialtask.services;

import com.example.trialtask.repositories.ExtraFeeRepository;
import com.example.trialtask.objects.BaseFee;
import com.example.trialtask.objects.ExtraFee;
import com.example.trialtask.repositories.BaseFeeRepository;
import com.example.trialtask.objects.WeatherData;
import com.example.trialtask.repositories.WeatherDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Service class for calculating delivery fees based on weather conditions and vehicle type
 * Uses business rules from the database tables base_fee and extra_fees
 */
@Service
public class DeliveryFeeServiceCRUD {
    private ExtraFeeRepository extraFeeRepository;
    private final WeatherDataRepository weatherDataRepository;
    private final BaseFeeRepository baseFeeRepository;

    /**
     * Constructs a new DeliveryFeeService with the provided WeatherDataRepository
     * @param weatherDataRepository the WeatherDataRepository used for fetching weather data
     */
    public DeliveryFeeServiceCRUD(WeatherDataRepository weatherDataRepository, BaseFeeRepository baseFeeRepository, ExtraFeeRepository extraFeeRepository) {
        this.extraFeeRepository = extraFeeRepository;
        this.weatherDataRepository = weatherDataRepository;
        this.baseFeeRepository = baseFeeRepository;
    }

    /**
     * Calculates the delivery fee for the given city and vehicle type
     * @param city        the city for which the delivery fee is calculated
     * @param vehicleType the type of vehicle used for delivery (car, scooter, bike)
     * @return the calculated delivery fee
     * @throws IllegalArgumentException if no weather data is found for the specified city
     */
    public double calculateDeliveryFee(String city, String vehicleType) {
        city = checkParameters(city, vehicleType);

        if (!isValidVehicleType(vehicleType)) {
            throw new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
        }
        WeatherData weatherData = weatherDataRepository.findFirstByStationNameOrderByObservationTimestampDesc(city);
        if (weatherData == null) {
            throw new IllegalArgumentException("No weather data found for the station: " + city);
        }
        return calculateDeliveryFeeExtracted(city, vehicleType, weatherData);
    }

    /**
     * Calculates the delivery fee for the given city, vehicle type, and datetime
     * @param city        the city for which the delivery fee is calculated
     * @param vehicleType the type of vehicle used for delivery (car, scooter, bike)
     * @param dateTime    the date and time for which the weather data is relevant
     * @return the calculated delivery fee
     * @throws IllegalArgumentException if no weather data is found for the specified city and datetime
     */
    public double calculateDeliveryFee(String city, String vehicleType, String dateTime) {
        city = checkParameters(city, vehicleType);
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
            long observationTimestamp = localDateTime.toEpochSecond(ZoneOffset.UTC);
            WeatherData weatherData = weatherDataRepository.findByStationNameAndObservationTimestamp(city, observationTimestamp);
            if (weatherData == null) {
                throw new IllegalArgumentException("No weather data found for the station: " + city + " at the specified date and time.");
            }
            return calculateDeliveryFeeExtracted(city, vehicleType, weatherData);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid dateTime parameter format: " + dateTime);
        }
    }

    /**
     * Checks if the parameters are there and changes the city names in order to find the information from the database correctly
    * @param city         the city which is being checked
     * @param vehicleType the vehicle which is being checked
     * @return the appended city name
     */
    private String checkParameters(String city, String vehicleType) {
        if (city == null) {
            throw new IllegalArgumentException("City cannot be null");
        }
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if(city.equals("Tallinn"))
            city += "-Harku";
        else if(city.equals("Tartu"))
            city += "-Tõravere";
        return city;
    }

    private double calculateDeliveryFeeExtracted(String city, String vehicleType, WeatherData weatherData) {
        double rbf = findRegionalBaseFee(city, vehicleType);
        double atef = calculateAirTemperatureExtraFee(vehicleType, weatherData.getAirTemperature());
        double wsef = calculateWindSpeedExtraFee(vehicleType, weatherData.getWindSpeed());
        double wpef = 0;
        if (weatherData.getPhenomenon() != null && !weatherData.getPhenomenon().equals("")) {
            wpef = calculateWeatherPhenomenonExtraFee(vehicleType, weatherData.getPhenomenon());
        }
        if (wsef == -1000 || wpef == -1000) {
            throw new IllegalArgumentException("Usage of selected vehicle type is forbidden.");
        }
        return rbf + atef + wsef + wpef;
    }
    private double findRegionalBaseFee(String city, String vehicleType) {
        BaseFee baseFee = baseFeeRepository.findByCityAndVehicleType(city.split("-")[0], vehicleType); // The city parameter is the same as the weather-data table, so only the first part is needed
        if (baseFee == null) {
            throw new IllegalArgumentException("No base fee found for the city: " + city + " and vehicle type: " + vehicleType);
        }
        return baseFee.getFee();
    }

    private double calculateAirTemperatureExtraFee(String vehicleType, double airTemperature) {
        List<ExtraFee> extraFees = extraFeeRepository.findByConditionTypeAndVehicleType("Air Temperature", vehicleType);
        for (ExtraFee extraFee : extraFees) {
            if (evaluateCondition(extraFee.getConditionValue(), airTemperature)) {
                return extraFee.getExtraFee();
            }
        }
        return 0.0;
    }
    private double calculateWindSpeedExtraFee(String vehicleType, double windSpeed) {
        List<ExtraFee> extraFees = extraFeeRepository.findByConditionTypeAndVehicleType("Wind Speed", vehicleType);
        for (ExtraFee extraFee : extraFees) {
            if (evaluateCondition(extraFee.getConditionValue(), windSpeed)) {
                return extraFee.getExtraFee();
            }
        }
        return 0.0;
    }

    /**
     * Finds if the weather phenomenon at the time of the delivery results in an extra fee
     * @param vehicleType       the vehicle type the fee is applicable to
     * @param weatherPhenomenon the weather phenomenon of the data
     * @return the extra fee
     */
    private double calculateWeatherPhenomenonExtraFee(String vehicleType, String weatherPhenomenon) {
        List<ExtraFee> extraFees = extraFeeRepository.findByConditionTypeAndVehicleType("Weather Phenomenon", vehicleType);
        for (ExtraFee extraFee : extraFees) {
            String conditionValue = extraFee.getConditionValue();
            if (conditionValue != null) {
                // splits the condition into parts by whitespace, removes all commas
                String[] conditions = conditionValue.toLowerCase().replaceAll(",", "").split("\\s+");
                List<String> conditionList = new ArrayList<>(Arrays.asList(conditions));
                conditionList.removeIf(condition -> condition.equalsIgnoreCase("or")); // removes all "or"s
                conditions = conditionList.toArray(new String[0]);

                for (String condition : conditions) {
                    condition = condition.trim();
                    if (weatherPhenomenon.toLowerCase().contains(condition.toLowerCase()) && !condition.isEmpty()) {
                        return extraFee.getExtraFee();
                    }
                }
            }
        }
        return 0.0;
    }

    /**
     * Finds if the weather condition is in a certain range
     * @param condition the condition that is being checked (wind, air temperature)
     * @param value the value of the condition of the weatherdata
     * @return true if the value is in the range, false if it not
     */
    private boolean evaluateCondition(String condition, double value) {
        if(condition != null){
            String[] parts = condition.split(" ");
            if (parts.length == 2) {
                if (parts[0].equals("<")) {
                    return value < Double.parseDouble(parts[1]);
                } else if (parts[0].equals(">")) {
                    return value > Double.parseDouble(parts[1]);
                }
            } else if (parts.length == 5) {
                return value >= Double.parseDouble(parts[1]) && value < Double.parseDouble(parts[4]);
            }
            return false;
        }
        return false;
    }

    private boolean isValidVehicleType(String vehicleType) {
        List<String> validVehicleTypes = Arrays.asList("car", "scooter", "bike");
        return validVehicleTypes.contains(vehicleType.toLowerCase());
    }
}
