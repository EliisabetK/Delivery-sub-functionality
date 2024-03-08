package com.example.trialtask.objects;

import jakarta.persistence.*;

@Table(name = "base_fee")
@Entity
public class BaseFee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String city;
    private String vehicleType;
    private double fee;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}