package com.example.trialtask.feesCRUD;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "extra_fees")
public class ExtraFee {

    @Id
    private Long id;

    private String conditionType;

    private String conditionValue;

    private Double extraFee;

    private String vehicleType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
