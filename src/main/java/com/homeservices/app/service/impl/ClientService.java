package com.homeservices.app.service.impl;

import com.homeservices.app.dto.request.ClientDetailsRequest;
import com.homeservices.app.dto.response.ClientDetailsResponse;
import com.homeservices.app.entity.ClientDetails;
import com.homeservices.app.entity.UserTable;
import com.homeservices.app.repository.ClientDetailsRepository;
import com.homeservices.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientDetailsRepository clientDetailsRepository;
    private final UserRepository userRepository;

    public ClientDetailsResponse getClientByUserId(Long userId) {
        ClientDetails details = clientDetailsRepository.findByClientId(userId)
                .orElseThrow(() -> new RuntimeException("Client details not found for user: " + userId));
        return toResponse(details);
    }

    public List<ClientDetailsResponse> getAllClients() {
        return clientDetailsRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClientDetailsResponse createOrUpdateClientDetails(Long userId, ClientDetailsRequest request) {
        UserTable user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        ClientDetails details = clientDetailsRepository.findByClientId(userId)
                .orElse(ClientDetails.builder().client(user).build());

        if (request.getBudget() != null) details.setBudget(request.getBudget());
        if (request.getJobDescription() != null) details.setJobDescription(request.getJobDescription());
        if (request.getUrgency() != null) details.setUrgency(request.getUrgency());
        if (request.getWorkType() != null) details.setWorkType(request.getWorkType());

        clientDetailsRepository.save(details);
        return toResponse(details);
    }

    // ── Mapper ─────────────────────────────────────────────────
    public ClientDetailsResponse toResponse(ClientDetails c) {
        String clientName = null;
        if (c.getClient() != null) clientName = c.getClient().getUsername();

        return ClientDetailsResponse.builder()
                .id(c.getId())
                .clientId(c.getClient() != null ? c.getClient().getId() : null)
                .clientName(clientName)
                .budget(c.getBudget())
                .jobDescription(c.getJobDescription())
                .urgency(c.getUrgency())
                .workType(c.getWorkType())
                .build();
    }
}
