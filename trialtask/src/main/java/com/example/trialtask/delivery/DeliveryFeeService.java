package com.example.trialtask.delivery;

import com.example.trialtask.weather.WeatherData;
import com.example.trialtask.weather.WeatherDataRepository;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class DeliveryFeeService {

    private final WeatherDataRepository weatherDataRepository;

    /**
     *
     * @param weatherDataRepository
     */
    public DeliveryFeeService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    /**
     *
     * @param city
     * @param vehicleType
     * @return
     */
    public double calculateDeliveryFee(String city, String vehicleType) {
        WeatherData weatherData = weatherDataRepository.findLatestByCity(city);

        double rbf = findRegionalBaseFee(city, vehicleType);
        double atef = calculateAirTemperatureExtraFee(vehicleType, weatherData.getAirTemperature());
        double wsef = calculateWindSpeedExtraFee(vehicleType, weatherData.getWindSpeed());
        double wpef = calculateWeatherPhenomenonExtraFee(vehicleType, weatherData.getPhenomenon());

        return rbf + atef + wsef + wpef;
    }
    public double calculateDeliveryFee(String city, String vehicleType, LocalDateTime dateTime) {
        long observationTimestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
        WeatherData weatherData = weatherDataRepository.findByCityAndDateTime(city, observationTimestamp);

        double rbf = findRegionalBaseFee(city, vehicleType);
        double atef = calculateAirTemperatureExtraFee(vehicleType, weatherData.getAirTemperature());
        double wsef = calculateWindSpeedExtraFee(vehicleType, weatherData.getWindSpeed());
        double wpef = calculateWeatherPhenomenonExtraFee(vehicleType, weatherData.getPhenomenon());

        return rbf + atef + wsef + wpef;
    }

    /**
     *
     * @param city
     * @param vehicleType
     * @return
     */
    private double findRegionalBaseFee(String city, String vehicleType) {
        if (city.equalsIgnoreCase("Tallinn")) {
            switch (vehicleType.toLowerCase()) {
                case "car":
                    return 4.0;
                case "scooter":
                    return 3.5;
                case "bike":
                    return 3.0;
            }
        } else if (city.equalsIgnoreCase("Tartu")) {
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

    private double calculateWeatherPhenomenonExtraFee(String vehicleType, String weatherPhenomenon) {
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
