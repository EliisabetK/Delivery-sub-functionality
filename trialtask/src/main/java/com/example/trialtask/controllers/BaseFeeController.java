package com.example.trialtask.controllers;

import com.example.trialtask.repositories.BaseFeeRepository;
import com.example.trialtask.objects.BaseFee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/base-fees")
public class BaseFeeController {

    private final BaseFeeRepository baseFeeRepository;

    public BaseFeeController(BaseFeeRepository baseFeeRepository) {
        this.baseFeeRepository = baseFeeRepository;
    }

    @Operation(summary = "Get all base fees")
    @GetMapping
    public List<BaseFee> getAllBaseFees() {
        return baseFeeRepository.findAll();
    }

    @Operation(summary = "Get base fee by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseFee> getBaseFee(
        @Parameter(description = "Base fee ID")
        @PathVariable Long id) {
            return baseFeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        }

    @Operation(summary = "Add a new base fee")
    @PostMapping
    public BaseFee createBaseFee(
        @Parameter(description = "City") @RequestParam String city,
        @Parameter(description = "Vehicle type") @RequestParam String vehicleType,
        @Parameter(description = "Fee") @RequestParam double fee) {
            BaseFee baseFee = new BaseFee();
            baseFee.setCity(city);
            baseFee.setVehicleType(vehicleType);
            baseFee.setFee(fee);
            return baseFeeRepository.save(baseFee);
        }

    @Operation(summary = "Update base fee by ID")
    @PutMapping("/{id}")
    public ResponseEntity<BaseFee> updateBaseFee(
        @Parameter(description = "Base fee ID")
        @PathVariable Long id,
        @RequestBody BaseFee baseFeeDetails) {
            return baseFeeRepository.findById(id)
                .map(baseFee -> {
                    baseFee.setCity(baseFeeDetails.getCity());
                    baseFee.setVehicleType(baseFeeDetails.getVehicleType());
                    baseFee.setFee(baseFeeDetails.getFee());
                    BaseFee updatedBaseFee = baseFeeRepository.save(baseFee);
                    return ResponseEntity.ok(updatedBaseFee);
                }).orElseGet(() -> ResponseEntity.notFound().build());
            }

    @Operation(summary = "Delete base fee by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBaseFee(
        @Parameter(description = "Base fee ID")
        @PathVariable Long id) {
            return baseFeeRepository.findById(id)
                .map(baseFee -> {
                    baseFeeRepository.delete(baseFee);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
        }
}
