package com.example.trialtask.controllers;

import com.example.trialtask.objects.ExtraFee;
import com.example.trialtask.repositories.ExtraFeeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Retrieves all the extra fees.
     * @return a list of all extra fees
     */
    @Operation(summary = "Get all extra fees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all extra fees."),
            @ApiResponse(responseCode = "404", description = "No extra fees found.")
    })
    @GetMapping
    public List<ExtraFee> getAllExtraFees() {
        return extraFeeRepository.findAll();
    }

    /**
     * Retrieves an extra fee by the ID
     * @param id the ID of the extra fee
     * @return the extra fee with the specified ID / 404 error if not found
     */
    @Operation(summary = "Get extra fee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found extra fee."),
            @ApiResponse(responseCode = "404", description = "Extra fee with ID not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExtraFee> getExtraFee(
            @Parameter(description = "Extra fee ID")
            @PathVariable Long id) {
        return extraFeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Adds a new extra fee
     * @param condition_type  the condition type for which the extra fee is applicable
     * @param condition_value the condition value for the extra fee (like the temperature or weather phenomenon)
     * @param fee             the extra fee amount
     * @param vehicleType     the type of vehicle for which the extra fee is applicable
     * @return the created extra fee / 400 error if invalid data provided
     */
    @Operation(summary = "Add a new extra fee. To forbid the use of a vehicle, enter -1000 as the fee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added the new extra fee."),
            @ApiResponse(responseCode = "400", description = "Error creating new extra fee.")
    })
    @PostMapping
    public ExtraFee extraFee(
            @Parameter(description = "Condition type") @RequestParam String condition_type,
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

    /**
     * Updates an extra fee by the ID
     * @param id             the ID of the extra fee to be updated
     * @param extraFeeDetails the details of the extra fee
     * @return the updated extra fee / 404 error if not found
     */
    @Operation(summary = "Update extra fee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated extra fee."),
            @ApiResponse(responseCode = "404", description = "Extra fee with given ID not found."),
            @ApiResponse(responseCode = "400", description = "Error updating base fee.")
    })
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

    /**
     * Deletes an extra fee by the ID
     * @param id the ID of the extra fee to be deleted
     * @return a success message or a 404 error if it is not found
     */
    @Operation(summary = "Delete extra fee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted extra fee."),
            @ApiResponse(responseCode = "404", description = "Extra fee with given ID not found.")
    })
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
