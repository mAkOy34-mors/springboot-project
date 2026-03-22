package com.homeservices.app.repository;

import com.homeservices.app.entity.ClientRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClientRatingRepository extends JpaRepository<ClientRating, Long> {
    List<ClientRating> findByClientId(Long clientId);
    List<ClientRating> findByWorkerId(Long workerId);
}
