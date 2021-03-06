package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.exception.AuthenticationException;
import com.bezshtanko.university_admission.exception.UserNotExistException;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.repository.UserRepository;
import com.bezshtanko.university_admission.transfer.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void login(User user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (!userFromDB.isPresent()
                || !passwordEncoder.matches(userFromDB.get().getPassword(), passwordEncoder.encode(user.getPassword()))) {
            throw new AuthenticationException();
        }
    }

    public UserDTO findByEmail(String email) {
        return new UserDTO(userRepository.findByEmail(email)
                .orElseThrow(UserNotExistException::new));
    }

    public UserDTO findById(Long id) {
        return new UserDTO(userRepository.findById(id)
                .orElseThrow(UserNotExistException::new));
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void blockUser(Long userId) {
        userRepository.blockUser(userId);
    }

    public void unblockUser(Long userId) {
        userRepository.unblockUser(userId);
    }

}
