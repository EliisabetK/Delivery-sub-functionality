package com.example.trialtask.weather;

import jakarta.persistence.*;

@Table(name = "weather_data")
@Entity
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String stationName;
    private String wmocode;
    private Double airTemperature;
    private Double windSpeed;
    private String phenomenon;
    private Long observationTimestamp;

    /**
     * Getters and setter for all the information that is collected
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getWmocode() {
        return wmocode;
    }

    public void setWmocode(String wmocode) {
        this.wmocode = wmocode;
    }

    public Double getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(Double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    public Long getObservationTimestamp() {
        return observationTimestamp;
    }

    public void setObservationTimestamp(Long observationTimestamp) {
        this.observationTimestamp = observationTimestamp;
    }
}
