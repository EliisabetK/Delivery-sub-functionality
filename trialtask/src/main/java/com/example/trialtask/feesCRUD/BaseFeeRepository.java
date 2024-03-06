package com.example.trialtask.feesCRUD;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseFeeRepository extends JpaRepository<BaseFee, Long> {
    BaseFee findByCityAndVehicleType(String city, String vehicleType);

    @Table(name = "extra_fees")
    @Entity
    class ExtraFee {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String conditionType;
        private String vehicleType;
        private String conditionValue;
        private double extraFee;

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

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getConditionValue() {
            return conditionValue;
        }

        public void setConditionValue(String conditionValue) {
            this.conditionValue = conditionValue;
        }

        public double getExtraFee() {
            return extraFee;
        }

        public void setExtraFee(double extraFee) {
            this.extraFee = extraFee;
        }
    }
}