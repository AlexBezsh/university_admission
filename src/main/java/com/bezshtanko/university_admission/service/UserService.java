package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.transfer.UserDTO;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.exception.DBException;
import com.bezshtanko.university_admission.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Page<UserDTO> getUsers(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(UserDTO::new);
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

}
