package com.homeservices.app.repository;

import com.homeservices.app.entity.WorkerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerDetailsRepository extends JpaRepository<WorkerDetails, Long> {
    Optional<WorkerDetails> findByWorkerId(Long workerId);
    List<WorkerDetails> findByWorkType(String workType);
    List<WorkerDetails> findByIsVerified(Boolean isVerified);
}
