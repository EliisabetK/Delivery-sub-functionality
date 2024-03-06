package com.example.trialtask.feesCRUD;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtraFeeRepository extends JpaRepository<BaseFeeRepository.ExtraFee, Long> {

}