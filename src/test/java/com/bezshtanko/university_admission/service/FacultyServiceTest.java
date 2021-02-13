package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.TestData;
import com.bezshtanko.university_admission.exception.FacultyNotExistException;
import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.faculty.FacultyStatus;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.repository.EnrollmentRepository;
import com.bezshtanko.university_admission.repository.FacultyRepository;
import com.bezshtanko.university_admission.repository.UserRepository;
import com.bezshtanko.university_admission.transfer.FacultyDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FacultyServiceTest {

    private Faculty testFaculty;

    @Mock
    private FacultyRepository facultyRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FacultyService testedInstance;

    @Before
    public void init() {
        testFaculty = Faculty.builder()
                .id(TestData.TEST_FACULTY.getId())
                .nameEn(TestData.TEST_FACULTY.getNameEn())
                .nameUa(TestData.TEST_FACULTY.getNameUa())
                .status(TestData.TEST_FACULTY.getStatus())
                .descriptionEn(TestData.TEST_FACULTY.getDescriptionEn())
                .descriptionUa(TestData.TEST_FACULTY.getDescriptionUa())
                .stateFundedPlaces(TestData.TEST_FACULTY.getStateFundedPlaces())
                .contractPlaces(TestData.TEST_FACULTY.getContractPlaces())
                .subjects(TestData.TEST_FACULTY.getSubjects())
                .build();

        reset(facultyRepository);
        reset(enrollmentRepository);
        reset(userRepository);
    }

    @Test
    public void shouldInvokeSaveMethodWhenSavingFaculty() {
        when(facultyRepository.save(testFaculty)).thenReturn(testFaculty);

        testedInstance.save(testFaculty);

        verify(facultyRepository, times(1)).save(testFaculty);
    }

    @Test
    public void shouldReturnFacultyWhenFacultyExist() {
        when(facultyRepository.findById(testFaculty.getId())).thenReturn(Optional.of(testFaculty));

        Faculty result = testedInstance.findById(testFaculty.getId());

        assertEquals(testFaculty, result);
    }

    @Test(expected = FacultyNotExistException.class)
    public void shouldThrowFacultyNotExistExceptionWhenFacultyNotExist() {
        when(facultyRepository.findById(isA(Long.class))).thenReturn(Optional.empty());

        testedInstance.findById(testFaculty.getId());
    }

    @Test
    public void shouldReturnPageOfFacultyDTOsWithRegistrationAllowedFalseWhenFindAllInvoked() {
        pageOfFacultiesShouldBeConvertedToPageOfFacultyDTOs(false, TestData.TEST_USER.getEnrollments());
    }

    @Test
    public void shouldReturnPageOfFacultyDTOsWithRegistrationAllowedTrueWhenFindAllInvoked() {
        pageOfFacultiesShouldBeConvertedToPageOfFacultyDTOs(true, Collections.emptyList());
    }

    private void pageOfFacultiesShouldBeConvertedToPageOfFacultyDTOs(boolean registrationAllowed, List<Enrollment> enrollments) {
        List<Faculty> faculties = Collections.singletonList(testFaculty);
        Page<Faculty> pageFromDB = new PageImpl<>(faculties);
        FacultyDTO expected = new FacultyDTO(testFaculty);
        expected.setRegistrationAllowed(registrationAllowed);

        when(facultyRepository.findAll(isA(Pageable.class))).thenReturn(pageFromDB);
        when(userRepository.findByEmail(TestData.TEST_USER.getEmail())).thenReturn(Optional.of(TestData.TEST_USER));
        when(enrollmentRepository.findAllByUserId(TestData.TEST_USER.getId())).thenReturn(enrollments);

        Page<FacultyDTO> pageFromService = testedInstance.findAllForUser(Pageable.unpaged(), TestData.TEST_USER);
        FacultyDTO actual = pageFromService.stream().collect(Collectors.toList()).get(0);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test(expected = FacultyNotExistException.class)
    public void shouldThrowFacultyNotExistExceptionWhenFacultyWithEnrollmentsNotExist() {
        when(facultyRepository.findById(isA(Long.class))).thenReturn(Optional.empty());

        testedInstance.getWithEnrollmentsForUser(1L, new User());
    }

    @Test
    public void shouldReturnFacultyDTOWithRelevantEnrollmentsWhenFacultyIsActive() {
        Long id = testFaculty.getId();
        testFaculty.setStatus(FacultyStatus.ACTIVE);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(testFaculty));
        when(enrollmentRepository.findRelevantByFacultyId(id)).thenReturn(TestData.TEST_FACULTY.getEnrollments());
        when(userRepository.findByEmail(TestData.TEST_USER.getEmail())).thenReturn(Optional.of(TestData.TEST_USER));
        when(enrollmentRepository.findAllByUserId(TestData.TEST_USER.getId())).thenReturn(TestData.TEST_USER.getEnrollments());

        FacultyDTO faculty = testedInstance.getWithEnrollmentsForUser(id, TestData.TEST_USER);

        verify(facultyRepository, times(1)).findById(id);
        verify(enrollmentRepository, times(1)).findRelevantByFacultyId(id);
        verify(userRepository, times(1)).findByEmail(TestData.TEST_USER.getEmail());
        verify(enrollmentRepository, times(1)).findAllByUserId(TestData.TEST_USER.getId());
        assertEquals(TestData.TEST_FACULTY.getEnrollments(), faculty.getEnrollments());
        assertFalse(faculty.isRegistrationAllowed());
    }

    @Test
    public void shouldReturnFacultyDTOWithFinalizedEnrollmentsAndRegistrationAllowedFalseWhenFacultyIsClosed() {
        Long id = testFaculty.getId();
        testFaculty.setStatus(FacultyStatus.CLOSED);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(testFaculty));
        when(enrollmentRepository.findFinalizedByFacultyId(id)).thenReturn(TestData.TEST_FACULTY.getEnrollments());

        FacultyDTO faculty = testedInstance.getWithEnrollmentsForUser(id, TestData.TEST_USER);

        verify(facultyRepository, times(1)).findById(id);
        verify(enrollmentRepository, times(1)).findFinalizedByFacultyId(id);
        assertEquals(TestData.TEST_FACULTY.getEnrollments(), faculty.getEnrollments());
        assertFalse(faculty.isRegistrationAllowed());
    }

    @Test
    public void shouldInvokeUpdateMethodWhenUpdatingFaculty() {
        doNothing().when(facultyRepository).updateFaculty(testFaculty);

        testedInstance.update(testFaculty);

        verify(facultyRepository, times(1)).updateFaculty(testFaculty);
    }

    @Test
    public void shouldInvokeRepositoriesMethodsInParticularOrderWhenFinalizingFaculty() {
        when(facultyRepository.findById(testFaculty.getId())).thenReturn(Optional.of(testFaculty));
        doNothing().when(facultyRepository).setClosed(testFaculty.getId());
        doNothing().when(enrollmentRepository).setFinalized(testFaculty.getId(), testFaculty.getTotalPlaces());
        doNothing().when(userRepository).setEnrolledStateFunded(testFaculty.getId(), testFaculty.getStateFundedPlaces());
        doNothing().when(userRepository).setEnrolledContract(testFaculty.getId(), testFaculty.getContractPlaces());

        InOrder inOrder = inOrder(facultyRepository, enrollmentRepository, userRepository);
        testedInstance.finalizeFaculty(testFaculty.getId());

        inOrder.verify(facultyRepository).findById(testFaculty.getId());
        inOrder.verify(facultyRepository).setClosed(testFaculty.getId());
        inOrder.verify(enrollmentRepository).setFinalized(testFaculty.getId(), testFaculty.getTotalPlaces());
        inOrder.verify(userRepository).setEnrolledStateFunded(testFaculty.getId(), testFaculty.getStateFundedPlaces());
        inOrder.verify(userRepository).setEnrolledContract(testFaculty.getId(), testFaculty.getContractPlaces());
    }

    @Test
    public void shouldInvokeDeleteMethodWhenDeletingFaculty() {
        doNothing().when(facultyRepository).deleteById(testFaculty.getId());

        testedInstance.deleteById(testFaculty.getId());

        verify(facultyRepository, times(1)).deleteById(testFaculty.getId());
    }

}
