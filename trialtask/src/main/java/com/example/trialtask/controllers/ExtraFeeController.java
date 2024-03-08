package com.example.trialtask.controllers;

import com.example.trialtask.objects.BaseFee;
import com.example.trialtask.repositories.ExtraFeeRepository;
import com.example.trialtask.objects.ExtraFee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/extra-fees")
public class ExtraFeeController {

    private final ExtraFeeRepository extraFeeRepository;

    public ExtraFeeController(ExtraFeeRepository extraFeeRepository) {
        this.extraFeeRepository = extraFeeRepository;
    }

    @Operation(summary = "Get all extra fees")
    @GetMapping
    public List<ExtraFee> getAllExtraFees() {
        return extraFeeRepository.findAll();
    }

    @Operation(summary = "Get extra fee by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ExtraFee> getExtraFee(
        @Parameter(description = "Extra fee ID")
        @PathVariable Long id) {
            return extraFeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        }

    @Operation(summary = "Add a new extra fee. To forbid the use of a vehicle, enter -1000 as the fee.")
    @PostMapping
    public ExtraFee extraFee(
        @Parameter(description = "Condition type") @RequestParam String condition_type ,
        @Parameter(description = "Condition value") @RequestParam String condition_value,
        @Parameter(description = "Extra fee") @RequestParam double fee,
        @Parameter(description = "Vehicle type") @RequestParam String vehicleType) {

        ExtraFee extraFee = new ExtraFee();
        extraFee.setConditionType(condition_type);
        extraFee.setConditionValue(condition_value);
        extraFee.setExtraFee(fee);
        extraFee.setVehicleType(vehicleType);
        return extraFeeRepository.save(extraFee);
    }

    @Operation(summary = "Update extra fee by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ExtraFee> updateExtraFee(
        @Parameter(description = "Extra fee ID")
        @PathVariable Long id,
        @RequestBody ExtraFee extraFeeDetails) {
            return extraFeeRepository.findById(id)
            .map(extraFee -> {
                extraFee.setConditionType(extraFeeDetails.getConditionType());
                extraFee.setVehicleType(extraFeeDetails.getVehicleType());
                extraFee.setConditionValue(extraFeeDetails.getConditionValue());
                extraFee.setExtraFee(extraFeeDetails.getExtraFee());
                ExtraFee updatedExtraFee = extraFeeRepository.save(extraFee);
                return ResponseEntity.ok(updatedExtraFee);
            }).orElseGet(() -> ResponseEntity.notFound().build());
        }

    @Operation(summary = "Delete extra fee by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExtraFee(
        @Parameter(description = "Extra fee ID")
        @PathVariable Long id) {
            return extraFeeRepository.findById(id)
                .map(extraFee -> {
                    extraFeeRepository.delete(extraFee);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
            }
}
