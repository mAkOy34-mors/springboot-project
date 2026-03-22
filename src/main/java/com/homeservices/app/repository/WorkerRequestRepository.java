package com.homeservices.app.repository;

import com.homeservices.app.entity.WorkerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRequestRepository extends JpaRepository<WorkerRequest, Long> {
    Optional<WorkerRequest> findByTrackingId(String trackingId);
    List<WorkerRequest> findByWorkerId(Long workerId);
    List<WorkerRequest> findByStatus(String status);
}
