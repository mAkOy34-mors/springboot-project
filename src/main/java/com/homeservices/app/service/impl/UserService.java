package com.homeservices.app.service.impl;

import com.homeservices.app.dto.request.UpdateProfileRequest;
import com.homeservices.app.dto.response.UserProfileResponse;
import com.homeservices.app.dto.response.UserResponse;
import com.homeservices.app.entity.UserProfile;
import com.homeservices.app.entity.UserRole;
import com.homeservices.app.entity.UserTable;
import com.homeservices.app.repository.UserProfileRepository;
import com.homeservices.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UserResponse getUserById(Long id) {
        UserTable user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return toUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        UserTable user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElse(UserProfile.builder().user(user).build());

        if (request.getName() != null) profile.setName(request.getName());
        if (request.getLastName() != null) profile.setLastName(request.getLastName());
        if (request.getAddress() != null) profile.setAddress(request.getAddress());

        userProfileRepository.save(profile);
        return toUserResponse(user);
    }

    @Transactional
    public UserResponse updateProfilePic(Long userId, String picUrl) {
        UserTable user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElse(UserProfile.builder().user(user).build());
        profile.setProfilePic(picUrl);
        userProfileRepository.save(profile);
        return toUserResponse(user);
    }

    @Transactional
    public void updateAvailability(Long userId, boolean available) {
        UserTable user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setIsAvailable(available);
        userRepository.save(user);
    }

    @Transactional
    public void updateGovId(Long userId, String govIdImagePath, String govIdNumber, String selfiePath) {
        UserTable user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setGovIdImagePath(govIdImagePath);
        user.setGovIdNumber(govIdNumber);
        user.setSelfiePath(selfiePath);
        userRepository.save(user);
    }

    // ── Mapper ─────────────────────────────────────────────────
    public UserResponse toUserResponse(UserTable user) {
        List<String> roles = user.getRoles().stream()
                .map(UserRole::getRoleName)
                .collect(Collectors.toList());

        UserProfileResponse profileResponse = null;
        if (user.getUserProfile() != null) {
            UserProfile p = user.getUserProfile();
            profileResponse = UserProfileResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .lastName(p.getLastName())
                    .address(p.getAddress())
                    .profilePic(p.getProfilePic())
                    .isVerified(p.getIsVerified())
                    .build();
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isAvailable(user.getIsAvailable())
                .timeStamp(user.getTimeStamp())
                .roles(roles)
                .profile(profileResponse)
                .build();
    }
}
