package com.example.trialtask.feesCRUD;

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

    @GetMapping
    public List<BaseFee> getAllBaseFees() {
        return baseFeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseFee> getBaseFee(@PathVariable Long id) {
        return baseFeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public BaseFee createBaseFee(@RequestParam String city, @RequestParam String vehicleType, @RequestParam double fee) {
        BaseFee baseFee = new BaseFee();
        baseFee.setCity(city);
        baseFee.setVehicleType(vehicleType);
        baseFee.setFee(fee);
        return baseFeeRepository.save(baseFee);
    }
    @PutMapping("/{id}")
    public ResponseEntity<BaseFee> updateBaseFee(@PathVariable Long id, @RequestBody BaseFee baseFeeDetails) {
        return baseFeeRepository.findById(id)
                .map(baseFee -> {
                    baseFee.setCity(baseFeeDetails.getCity());
                    baseFee.setVehicleType(baseFeeDetails.getVehicleType());
                    baseFee.setFee(baseFeeDetails.getFee());
                    BaseFee updatedBaseFee = baseFeeRepository.save(baseFee);
                    return ResponseEntity.ok(updatedBaseFee);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBaseFee(@PathVariable Long id) {
        return baseFeeRepository.findById(id)
                .map(baseFee -> {
                    baseFeeRepository.delete(baseFee);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}