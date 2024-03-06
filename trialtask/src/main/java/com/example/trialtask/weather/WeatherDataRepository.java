package com.example.trialtask.weather;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    WeatherData findFirstByStationNameOrderByObservationTimestampDesc(String stationName);
    WeatherData findByStationNameAndObservationTimestamp(String stationName, Long observationTimestamp);
}