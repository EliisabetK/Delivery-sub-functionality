package com.example.trialtask.repositories;

import com.example.trialtask.objects.BaseFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseFeeRepository extends JpaRepository<BaseFee, Long> {
    BaseFee findByCityAndVehicleType(String city, String vehicleType);
}