package com.example.trialtask.objects;

import jakarta.persistence.*;

@Entity
@Table(name = "extra_fees")
public class ExtraFee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String conditionType;

    private String conditionValue;

    private Double extraFee;

    private String vehicleType;
    public ExtraFee() {
    }

    public ExtraFee(String conditionType, String conditionValue, double extraFee, String vehicleType) {
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.extraFee = extraFee;
        this.vehicleType = vehicleType;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }

    public Double getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(Double extraFee) {
        this.extraFee = extraFee;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
