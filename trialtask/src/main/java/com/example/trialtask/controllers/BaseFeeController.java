package com.example.trialtask.controllers;

import com.example.trialtask.repositories.BaseFeeRepository;
import com.example.trialtask.objects.BaseFee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Gets all base fees
     * @return a list of all base fees
     */
    @Operation(summary = "Get all base fees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all base fees."),
            @ApiResponse(responseCode = "400", description = "Base fees not found.")
    })
    @GetMapping
    public List<BaseFee> getAllBaseFees() {
        return baseFeeRepository.findAll();
    }

    /**
     * Gets a base fee by ID.
     * @param id the ID of the base fee
     * @return the base fee with the specified ID, or a 404 error if not found
     */
    @Operation(summary = "Get base fee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found base fee."),
            @ApiResponse(responseCode = "404", description = "Base fee with ID not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseFee> getBaseFee(
            @Parameter(description = "Base fee ID")
            @PathVariable Long id) {
        return baseFeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Adds a new base fee
     * @param city        the city for which the base fee is added
     * @param vehicleType the type of vehicle the base fee applies to
     * @param fee         the fee amount
     * @return the created base fee
     */
    @Operation(summary = "Add a new base fee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Base fee successfully added."),
            @ApiResponse(responseCode = "400", description = "Error creating base fee."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
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

    /**
     * Updates a base fee by ID
     * @param id            the ID of the base fee that is updated
     * @param baseFeeDetails the details of the base fee to be updated
     * @return the updated base fee / or a 404 error it is not found
     */
    @Operation(summary = "Update base fee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Base fee updated."),
            @ApiResponse(responseCode = "404", description = "Base fee with ID not found."),
            @ApiResponse(responseCode = "400", description = "Error updating base fee.")
    })
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

    /**
     * Deletes a base fee by ID
     * @param id the ID of the base fee to be deleted
     * @return a success message or a 404 error if it is not found
     */
    @Operation(summary = "Delete base fee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Base fee deleted."),
            @ApiResponse(responseCode = "404", description = "Base fee with ID not found.")
    })
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
