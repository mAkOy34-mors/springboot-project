package com.homeservices.app.repository;

import com.homeservices.app.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByWorkerId(Long workerId);
    List<Rating> findByClientId(Long clientId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.worker.id = :workerId")
    Double getAverageRatingByWorkerId(Long workerId);
}
