package com.example.trialtask.weather;

import jakarta.persistence.*;

@Table(name = "weather_data")
@Entity
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String city;
    private String wmocode;
    private Double airTemperature;
    private Double windSpeed;
    private String phenomenon;
    private Long dateTime;

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
        return city;
    }

    public void setStationName(String stationName) {
        this.city = stationName;
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
        return dateTime;
    }

    public void setObservationTimestamp(Long observationTimestamp) {
        this.dateTime = observationTimestamp;
    }
}
