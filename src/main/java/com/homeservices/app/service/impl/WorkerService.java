package com.homeservices.app.service.impl;

import com.homeservices.app.dto.request.WorkerDetailsRequest;
import com.homeservices.app.dto.response.SearchWorkerResponse;
import com.homeservices.app.dto.response.WorkerDetailsResponse;
import com.homeservices.app.dto.response.WorkerDocumentResponse;
import com.homeservices.app.entity.UserProfile;
import com.homeservices.app.entity.UserTable;
import com.homeservices.app.entity.WorkerDetails;
import com.homeservices.app.entity.WorkerDocument;
import com.homeservices.app.repository.UserRepository;
import com.homeservices.app.repository.WorkerDetailsRepository;
import com.homeservices.app.repository.WorkerDocumentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final WorkerDetailsRepository workerDetailsRepository;
    private final WorkerDocumentRepository workerDocumentRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public List<WorkerDetailsResponse> getAllWorkers() {
        return workerDetailsRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<WorkerDetailsResponse> getWorkersByType(String workType) {
        return workerDetailsRepository.findByWorkType(workType).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public WorkerDetailsResponse getWorkerByUserId(Long userId) {
        WorkerDetails details = workerDetailsRepository.findByWorkerId(userId)
                .orElseThrow(() -> new RuntimeException("Worker details not found for user: " + userId));
        return toResponse(details);
    }

    public WorkerDetailsResponse getWorkerById(Long id) {
        WorkerDetails details = workerDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker details not found with id: " + id));
        return toResponse(details);
    }

    @Transactional
    public WorkerDetailsResponse createOrUpdateWorkerDetails(Long userId, WorkerDetailsRequest request) {
        UserTable user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        WorkerDetails details = workerDetailsRepository.findByWorkerId(userId)
                .orElse(WorkerDetails.builder().worker(user).build());

        if (request.getWorkType() != null) details.setWorkType(request.getWorkType());
        if (request.getWorkExperience() != null) details.setWorkExperience(request.getWorkExperience());
        if (request.getReviews() != null) details.setReviews(request.getReviews());
        if (request.getComments() != null) details.setComments(request.getComments());

        workerDetailsRepository.save(details);
        return toResponse(details);
    }

    @Transactional
    public WorkerDetailsResponse addDocument(Long userId, String fileName, String fileUrl) {
        WorkerDetails details = workerDetailsRepository.findByWorkerId(userId)
                .orElseThrow(() -> new RuntimeException("Worker details not found for user: " + userId));

        WorkerDocument doc = WorkerDocument.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .workerDetails(details)
                .build();
        workerDocumentRepository.save(doc);

        // Also update the documents JSON array
        List<String> docs = parseDocuments(details.getDocuments());
        docs.add(fileUrl);
        try {
            details.setDocuments(objectMapper.writeValueAsString(docs));
        } catch (Exception e) {
            details.setDocuments("[]");
        }
        workerDetailsRepository.save(details);

        return toResponse(details);
    }

    @Transactional
    public void verifyWorker(Long workerDetailsId, boolean verified) {
        WorkerDetails details = workerDetailsRepository.findById(workerDetailsId)
                .orElseThrow(() -> new RuntimeException("Worker details not found: " + workerDetailsId));
        details.setIsVerified(verified);
        workerDetailsRepository.save(details);
    }

    // ── Mapper ─────────────────────────────────────────────────
    public WorkerDetailsResponse toResponse(WorkerDetails d) {
        List<WorkerDocumentResponse> docs = d.getWorkerDocuments().stream()
                .map(doc -> WorkerDocumentResponse.builder()
                        .id(doc.getId())
                        .fileName(doc.getFileName())
                        .fileUrl(doc.getFileUrl())
                        .build())
                .collect(Collectors.toList());

        String workerName = null;
        String profilePic = null;
        if (d.getWorker() != null) {
            workerName = d.getWorker().getUsername();
            if (d.getWorker().getUserProfile() != null) {
                profilePic = d.getWorker().getUserProfile().getProfilePic();
            }
        }

        return WorkerDetailsResponse.builder()
                .id(d.getId())
                .workerId(d.getWorker() != null ? d.getWorker().getId() : null)
                .workerName(workerName)
                .profilePic(profilePic)
                .workType(d.getWorkType())
                .workExperience(d.getWorkExperience())
                .reviews(d.getReviews())
                .comments(d.getComments())
                .ratings(d.getRatings())
                .isVerified(d.getIsVerified())
                .documents(parseDocuments(d.getDocuments()))
                .workerDocuments(docs)
                .build();
    }

    private List<String> parseDocuments(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<WorkerDetailsResponse> searchByName(String name, String lastName) {
        return workerDetailsRepository.findAll().stream()
                .filter(w -> w.getWorker() != null
                        && w.getWorker().getUserProfile() != null
                        && w.getWorker().getUserProfile().getName() != null
                        && w.getWorker().getUserProfile().getName()
                                .equalsIgnoreCase(name)
                        && w.getWorker().getUserProfile().getLastName() != null
                        && w.getWorker().getUserProfile().getLastName()
                                .equalsIgnoreCase(lastName))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<WorkerDetailsResponse> searchWorkers(String workType) {
        if (workType != null && !workType.isBlank()) {
            return workerDetailsRepository.findByWorkType(workType).stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        }
        return getAllWorkers();
    }
    public List<SearchWorkerResponse> searchWorkersForClient(String workType) {
        List<WorkerDetails> workers = (workType != null && !workType.isBlank())
                ? workerDetailsRepository.findByWorkType(workType)
                : workerDetailsRepository.findAll();

        return workers.stream()
                .map(this::toSearchResponse)
                .collect(Collectors.toList());
    }

    private SearchWorkerResponse toSearchResponse(WorkerDetails d) {
        String name = null;
        String lastName = null;
        String address = null;
        String profilePic = null;

        if (d.getWorker() != null && d.getWorker().getUserProfile() != null) {
            UserProfile p = d.getWorker().getUserProfile();
            name = p.getName();
            lastName = p.getLastName();
            address = p.getAddress();
            profilePic = p.getProfilePic();
        }

        // Get latest comment from ratings
        String latestComment = null;
        if (d.getRatingsReceived() != null && !d.getRatingsReceived().isEmpty()) {
            latestComment = d.getRatingsReceived()
                    .get(d.getRatingsReceived().size() - 1)
                    .getComments();
        }

        int totalRatings = d.getRatingsReceived() != null
                ? d.getRatingsReceived().size() : 0;

        return SearchWorkerResponse.builder()
                .id(d.getWorker() != null ? d.getWorker().getId() : null)
                .name(name)
                .lastName(lastName)
                .address(address)
                .workType(d.getWorkType())
                .workExperience(d.getWorkExperience())
                .profilePic(profilePic)
                .isVerified(d.getIsVerified())
                .documents(d.getDocuments())   // ← raw JSON string
                .averageRating(d.getRatings())
                .totalRatings(totalRatings)
                .latestComment(latestComment)
                .build();
    }
}
