package com.example.trialtask.feesCRUD;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseFeeRepository extends JpaRepository<BaseFee, Long> {
    BaseFee findByCityAndVehicleType(String city, String vehicleType);
}