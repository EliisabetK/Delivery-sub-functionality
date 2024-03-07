package com.example.trialtask.feesCRUD;

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

    @GetMapping
    public List<ExtraFee> getAllExtraFees() {
        return extraFeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtraFee> getExtraFee(@PathVariable Long id) {
        return extraFeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ExtraFee createExtraFee(@RequestBody ExtraFee extraFee) {
        return extraFeeRepository.save(extraFee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExtraFee> updateExtraFee(@PathVariable Long id, @RequestBody ExtraFee extraFeeDetails) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExtraFee(@PathVariable Long id) {
        return extraFeeRepository.findById(id)
                .map(extraFee -> {
                    extraFeeRepository.delete(extraFee);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
