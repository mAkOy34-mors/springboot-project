package com.homeservices.app.repository;

import com.homeservices.app.entity.BrgyPersonnelDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BrgyPersonnelDetailsRepository extends JpaRepository<BrgyPersonnelDetails, Long> {
    Optional<BrgyPersonnelDetails> findByBrgyPersonnelId(Long personnelId);
}
