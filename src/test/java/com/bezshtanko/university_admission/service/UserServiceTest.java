package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.TestData;
import com.bezshtanko.university_admission.exception.AuthenticationException;
import com.bezshtanko.university_admission.exception.UserNotExistException;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.repository.UserRepository;
import com.bezshtanko.university_admission.transfer.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService testedInstance;

    private User testUser;

    @Before
    public void init() {
        testUser = User.builder()
                .id(TestData.TEST_USER.getId())
                .fullName(TestData.TEST_USER.getFullName())
                .email(TestData.TEST_USER.getEmail())
                .password(TestData.TEST_USER.getPassword())
                .status(TestData.TEST_USER.getStatus())
                .roles(TestData.TEST_USER.getRoles())
                .city(TestData.TEST_USER.getCity())
                .region(TestData.TEST_USER.getRegion())
                .education(TestData.TEST_USER.getEducation())
                .build();

        reset(userRepository);
        reset(passwordEncoder);
    }

    @Test(expected = AuthenticationException.class)
    public void shouldThrowAuthenticationExceptionWhenUserNotExist() {
        String email = "email";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        testedInstance.login(User.builder().email(email).password("password").build());
    }

    @Test(expected = AuthenticationException.class)
    public void shouldThrowExceptionWhenPasswordIsWrong() {
        testUser.setPassword(TestData.TEST_USER_HASHED_PASSWORD);
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        testedInstance.login(User.builder().email(testUser.getEmail()).password("some random password").build());
    }

    @Test
    public void shouldDoNothingWhenEmailAndPasswordAreValid() {
        testUser.setPassword(TestData.TEST_USER_HASHED_PASSWORD);
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        testedInstance.login(testUser);

        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    public void ShouldReturnUserDTOWhenUserExist() {
        String email = "some@email";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        UserDTO result = testedInstance.findByEmail(email);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("enrollments")
                .isEqualTo(TestData.TEST_USER_DTO);
    }

    @Test(expected = UserNotExistException.class)
    public void shouldThrowUserNotExistExceptionWhenUserNotExist() {
        when(userRepository.findByEmail(isA(String.class))).thenReturn(Optional.empty());

        testedInstance.findByEmail("email@e");
    }

    @Test
    public void shouldInvokeSaveMethodInDaoWhenSavingNewUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);

        testedInstance.save(testUser);

        verify(userRepository, times(1)).save(testUser);
    }

    @Test(expected = UserNotExistException.class)
    public void shouldThrowUserNotExistExceptionWhenGetWithEnrollmentsInvokedForNonExistingUser() {
        when(userRepository.findById(isA(Long.class))).thenReturn(Optional.empty());

        testedInstance.findById(testUser.getId());
    }

    @Test
    public void shouldReturnUserDTOWhenUserExist() {
        Long id = testUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(testUser));

        UserDTO user = testedInstance.findById(id);

        assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("enrollments")
                .isEqualTo(TestData.TEST_USER_DTO);
    }

    @Test
    public void shouldInvokeBlockUserMethodWhenBlockingUser() {
        Long id = testUser.getId();
        doNothing().when(userRepository).blockUser(id);

        testedInstance.blockUser(id);

        verify(userRepository, times(1)).blockUser(id);
    }

    @Test
    public void shouldInvokeUnblockUserMethodWhenUnblockingUser() {
        Long id = testUser.getId();
        doNothing().when(userRepository).unblockUser(id);

        testedInstance.unblockUser(id);

        verify(userRepository, times(1)).unblockUser(id);
    }

}
