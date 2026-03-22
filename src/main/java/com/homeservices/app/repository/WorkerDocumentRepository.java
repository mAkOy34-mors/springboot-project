package com.homeservices.app.repository;

import com.homeservices.app.entity.WorkerDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkerDocumentRepository extends JpaRepository<WorkerDocument, Long> {
    List<WorkerDocument> findByWorkerDetailsId(Long workerDetailsId);
    void deleteByWorkerDetailsId(Long workerDetailsId);
}
