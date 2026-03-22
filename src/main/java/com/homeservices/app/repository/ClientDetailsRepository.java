package com.homeservices.app.repository;

import com.homeservices.app.entity.ClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientDetailsRepository extends JpaRepository<ClientDetails, Long> {
    Optional<ClientDetails> findByClientId(Long clientId);
}
