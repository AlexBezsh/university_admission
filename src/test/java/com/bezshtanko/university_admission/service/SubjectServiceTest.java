package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.TestData;
import com.bezshtanko.university_admission.model.subject.Subject;
import com.bezshtanko.university_admission.repository.SubjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService testedInstance;

    @Before
    public void init() {
        reset(subjectRepository);
    }

    @Test
    public void shouldReturnListOfSubjectsWhenFindAllInvoked() {
        List<Subject> expected = Arrays.asList(TestData.TEST_SUBJECT_1, TestData.TEST_SUBJECT_2);
        when(subjectRepository.findAll()).thenReturn(expected);

        List<Subject> actual = testedInstance.getAll();

        assertEquals(expected, actual);
    }

}
