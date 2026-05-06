package com.projectit210.service.impl;

import com.projectit210.dto.request.ProfileUpdateRequest;
import com.projectit210.entity.User;
import com.projectit210.enums.Gender;
import com.projectit210.exception.ResourceNotFoundException;
import com.projectit210.repository.UserRepository;
import com.projectit210.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Triển khai ProfileService - Quản lý hồ sơ cá nhân (CORE-03)
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    @Override
    public User getProfile(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
    }

    @Override
    @Transactional
    public User updateProfile(String userId, ProfileUpdateRequest request) {
        User user = getProfile(userId);

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getGender() != null) {
            try {
                user.setGender(Gender.valueOf(request.getGender().toUpperCase()));
            } catch (IllegalArgumentException ignored) {
                // Bỏ qua nếu giá trị không hợp lệ
            }
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }

        return userRepository.save(user);
    }
}
