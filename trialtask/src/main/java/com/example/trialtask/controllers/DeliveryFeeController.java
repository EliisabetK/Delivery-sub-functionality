package com.example.trialtask.controllers;

import com.example.trialtask.services.DeliveryFeeServiceCRUD;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

// Interface is available here: http://localhost:8080/swagger-ui/index.html
@RestController
@RequestMapping("/delivery-fees")
public class DeliveryFeeController {

    private final DeliveryFeeServiceCRUD deliveryFeeServiceCRUD;

    public DeliveryFeeController(DeliveryFeeServiceCRUD deliveryFeeServiceCRUD) {
        this.deliveryFeeServiceCRUD = deliveryFeeServiceCRUD;
    }
    /**
     * Calculates the delivery fee for a given city and vehicle type at a specified time
     * @param city        Name of the city
     * @param vehicleType Type of vehicle
     * @param dateTime    Date and time of the delivery
     * @return The calculated delivery fee
     * @throws IllegalArgumentException In case the input parameters are invalid
     */
    @Operation(summary = "Calculate delivery fee", description = "Calculate the delivery fee for a given city and vehicle type at a specified time (time has to be in the format of YYYY-MM-DDThh:mm).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery fee calculation successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping
    public ResponseEntity<?> calculateDeliveryFee(
            @Parameter(description = "Name of the city (case sensitive)", required = true) @RequestParam String city,
            @Parameter(description = "Type of vehicle (case sensitive)", required = true) @RequestParam String vehicleType,
            @Parameter(description = "Date and time of delivery (optional)") @RequestParam(required = false) String dateTime) {
        try {
            double fee;
            if (dateTime == null) {
                fee = deliveryFeeServiceCRUD.calculateDeliveryFee(city, vehicleType);
            } else {
                fee = deliveryFeeServiceCRUD.calculateDeliveryFee(city, vehicleType, dateTime);
            }
            return ResponseEntity.ok(fee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
