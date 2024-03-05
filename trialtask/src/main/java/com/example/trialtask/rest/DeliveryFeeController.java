package com.example.trialtask.rest;

import com.example.trialtask.delivery.DeliveryFeeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/delivery-fees")
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;

    public DeliveryFeeController(DeliveryFeeService deliveryFeeService) {
        this.deliveryFeeService = deliveryFeeService;
    }

    @GetMapping
    public ResponseEntity<?> calculateDeliveryFee(@RequestParam String city, @RequestParam String vehicleType, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        try {
            double fee;
            if (dateTime == null) {
                fee = deliveryFeeService.calculateDeliveryFee(city, vehicleType);
            } else {
                fee = deliveryFeeService.calculateDeliveryFee(city, vehicleType, dateTime);
            }
            return ResponseEntity.ok(fee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

