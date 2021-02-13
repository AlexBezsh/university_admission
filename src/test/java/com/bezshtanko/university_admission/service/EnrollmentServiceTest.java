package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.TestData;
import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.enrollment.EnrollmentStatus;
import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.repository.EnrollmentRepository;
import com.bezshtanko.university_admission.repository.MarkRepository;
import com.bezshtanko.university_admission.transfer.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private MarkRepository markRepository;

    @InjectMocks
    private EnrollmentService testedInstance;

    @Before
    public void init() {
        reset(markRepository);
        reset(enrollmentRepository);
    }

    @Test
    public void shouldInitializeEnrollmentAndInvokeSaveMethodWhenSavingNewEnrollment() {
        Enrollment testEnrollment = Enrollment
                .builder()
                .marks(TestData.TEST_ENROLLMENT.getMarks())
                .faculty(Faculty.builder().id(TestData.TEST_FACULTY.getId()).build())
                .build();
        UserDTO user = UserDTO.builder().id(1L).build();

        ArgumentCaptor<Enrollment> captor = ArgumentCaptor.forClass(Enrollment.class);
        when(enrollmentRepository.save(testEnrollment)).thenReturn(testEnrollment);

        testedInstance.saveNewEnrollment(testEnrollment, user);

        verify(enrollmentRepository).save(captor.capture());
        Enrollment enrollment = captor.getValue();

        assertEquals(user.getId(), enrollment.getUser().getId());
        assertEquals(TestData.TEST_ENROLLMENT.getMarks(), enrollment.getMarks());
        assertSame(enrollment.getStatus(), EnrollmentStatus.NEW);
    }

    @Test
    public void shouldReturnListOfEnrollmentsWhenFindAllByUserIdInvoked() {
        List<Enrollment> expected = TestData.TEST_USER.getEnrollments();
        when(enrollmentRepository.findAllByUserId(TestData.TEST_USER.getId())).thenReturn(expected);

        List<Enrollment> actual = testedInstance.findAllByUserId(TestData.TEST_USER.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldInvokeSetApprovedMethodWhenApprovingEnrollment() {
        Long id = TestData.TEST_USER.getId();
        doNothing().when(enrollmentRepository).setApproved(id);

        testedInstance.setApproved(id);

        verify(enrollmentRepository, times(1)).setApproved(id);
    }

}
