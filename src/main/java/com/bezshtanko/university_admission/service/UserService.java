package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.exception.DBException;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.model.user.UserStatus;
import com.bezshtanko.university_admission.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DBException("There is no such user in database"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new DBException("There is no such user in database"));
    }

    public void login(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            if (!passwordEncoder.matches(u.getPassword(), passwordEncoder.encode(user.getPassword()))) {
                throw new DBException();
            }
        });
    }

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new DBException("Error occurred");
        }
    }

    public void blockUser(Long userId) {
        userRepository.blockUser(userId);
    }

    public void unblockUser(Long userId) {
        userRepository.unblockUser(userId);
    }

    public void setEnrolled(List<User> users) {
        userRepository.setEnrolled(users
                .stream()
                .map(User::getId)
                .collect(Collectors.toList()));
    }

}
