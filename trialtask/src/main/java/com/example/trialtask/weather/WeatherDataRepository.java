package com.example.trialtask.weather;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    WeatherData findLatestByCity(String stationName);

    WeatherData findByCityAndDateTime(String stationName, Long observationTimestamp);

}