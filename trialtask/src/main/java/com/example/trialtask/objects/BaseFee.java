package com.example.trialtask.objects;

import jakarta.persistence.*;

@Entity
@Table(name = "base_fee")
public class BaseFee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String city;

    private String vehicleType;

    private double fee;

    /**
     * Getters and setter for all the information that is collected
     */
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
