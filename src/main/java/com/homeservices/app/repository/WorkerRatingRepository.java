package com.homeservices.app.repository;

import com.homeservices.app.entity.WorkerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkerRatingRepository extends JpaRepository<WorkerRating, Long> {
    List<WorkerRating> findByWorkerId(Long workerId);
    List<WorkerRating> findByClientId(Long clientId);

    @Query("SELECT AVG(r.rating) FROM WorkerRating r WHERE r.worker.id = :workerId")
    Double getAverageRatingByWorkerId(Long workerId);
}
